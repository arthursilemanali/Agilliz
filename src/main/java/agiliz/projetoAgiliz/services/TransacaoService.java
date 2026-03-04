package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.transacao.TransacaoRequest;
import agiliz.projetoAgiliz.dto.transacao.TransacaoResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Transacao;
import agiliz.projetoAgiliz.repositories.ITransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TransacaoService {
    private final ITransacaoRepository repository;
    private final CarteiraService carteiraService;
    private final ColaboradorService colaboradorService;

    public TransacaoResponse inserir(TransacaoRequest dto) {
        Transacao transacao = new Transacao();
        transacao.setColaborador(colaboradorService.getPorId(dto.colaborador()));
        BeanUtils.copyProperties(dto, transacao);

        carteiraService.validarTransacao(transacao);

        TransacaoResponse response = new TransacaoResponse(repository.save(transacao));
        carteiraService.solicitarSaque(transacao);
        return response;
    }

    public Page<TransacaoResponse> getTransacoesPaginadas(int pagina, int tamanho) {
        return repository.findAllResponse(PageRequest.of(pagina, tamanho));
    }

    public TransacaoResponse getResponsePorId(UUID id) {
        return new TransacaoResponse(getPorId(id));
    }

    public Transacao getPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(
                    () -> new ResponseEntityException(
                            HttpStatus.NOT_FOUND,
                            "Transação não encontrada com ID " + id,
                            404
                        )
                );
    }

    public void deletarPorId(UUID id) {
        if(!repository.existsById(id)) {
            throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Transação não encontrada com ID " + id, 404);
        }

        repository.deleteById(id);
    }

    public Page<TransacaoResponse> getTransacoesColaborador(UUID idColaborador, int pagina, int tamanho) {
        return repository.findByColaboradorResponse(PageRequest.of(pagina, tamanho), idColaborador);
    }

    public void realizarTransacoesAut(List<Colaborador> colaboradores) {
        List<Transacao> transacoes = colaboradores.stream()
                .map(Transacao::new)
                .toList();

        carteiraService.solicitarSaquesAut(transacoes);
        repository.saveAll(transacoes);
    }
}
