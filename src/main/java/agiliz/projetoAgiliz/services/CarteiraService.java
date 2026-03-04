package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.carteira.*;
import agiliz.projetoAgiliz.models.*;
import agiliz.projetoAgiliz.repositories.ICarteiraRepository;
import agiliz.projetoAgiliz.repositories.IEmissaoPagamentoRepository;
import agiliz.projetoAgiliz.repositories.IPacoteRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarteiraService {
    private static final Logger logger = LogManager.getLogger(CarteiraService.class);
    private final ICarteiraRepository repository;
    private final ColaboradorService colaboradorService;
    private final IPacoteRepository pacoteRepository;
    private final IEmissaoPagamentoRepository emissaoPagamentoRepository;

    public void solicitarSaque(Transacao transacao) {
        if (transacao.getColaborador().getCarteira() == null) throw new IllegalArgumentException("Carteira inexistente");
        emissaoPagamentoRepository.save(new EmissaoPagamento(transacao));
        sacar(transacao.getValor(), transacao.getColaborador().getIdColaborador());
    }

    public void solicitarSaquesAut(List<Transacao> transacoes) {
        List<EmissaoPagamento> emissoes = transacoes.stream()
                .map(EmissaoPagamento::new)
                .toList();

        emissaoPagamentoRepository.saveAll(emissoes);

        Set<UUID> carteirasIds = transacoes.stream()
                .map(t -> t.getColaborador().getCarteira().getIdCarteira())
                .collect(Collectors.toSet());

        repository.zerarSaldos(carteirasIds);
    }

    public CarteiraResponse cadastro(CarteiraPostRequest dto) {
        logger.info("[CarteiraService.cadastro] Iniciando cadastro de carteira para colaborador: {}", dto.fkColaborador());

        try {
            var carteira = new Carteira();
            carteira.setColaborador(colaboradorService.getPorId(dto.fkColaborador()));
            BeanUtils.copyProperties(dto, carteira);

            CarteiraResponse response = new CarteiraResponse(repository.save(carteira));
            logger.info("[CarteiraService.cadastro] Carteira cadastrada com sucesso para colaborador: {}", dto.fkColaborador());
            return response;
        } catch (Exception e) {
            logger.error("[CarteiraService.cadastro] Erro ao cadastrar carteira para colaborador: {}", dto.fkColaborador(), e);
            throw e;
        }
    }

    public List<CarteiraResponse> getCarteiraResponse() {
        logger.info("[CarteiraService.getCarteiraResponse] Buscando todas as carteiras.");
        return repository.findAllResponse();
    }

    public CarteiraResponse getCarteiraResponsePorId(UUID id) {
        logger.info("[CarteiraService.getCarteiraResponsePorId] Buscando carteira com id: {}", id);
        return new CarteiraResponse(getPorId(id));
    }

    public Carteira getPorId(UUID id) {
        logger.info("[CarteiraService.getPorId] Buscando carteira com id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new ResponseEntityException(
                        HttpStatusCode.valueOf(404), "Carteira com o id fornecido não existe", 404)
                );
    }

    public CarteiraResponse alterar(UUID id, CarteiraPutRequest dto) {
        logger.info("[CarteiraService.alterar] Alterando carteira com id: {}", id);
        try {
            var dados = getPorId(id);
            BeanUtils.copyProperties(dto, dados);
            CarteiraResponse response = new CarteiraResponse(repository.save(dados));
            logger.info("[CarteiraService.alterar] Carteira alterada com sucesso para id: {}", id);
            return response;
        } catch (Exception e) {
            logger.error("[CarteiraService.alterar] Erro ao alterar carteira com id: {}", id, e);
            throw e;
        }
    }

    public void sacar(Double valor, UUID idColaborador) {
        repository.descontarSaldo(valor,
                colaboradorService.getPorId(idColaborador)
        );
    }

    public void validarTransacao(Transacao transacao) {
        Double saldoCarteira = repository.findSaldoCarteira(transacao.getColaborador())
                .orElseThrow(() -> new ResponseEntityException(
                    HttpStatus.NOT_FOUND,
                    "Colaborador não possui carteira cadastrada",
                    404
                )
        );

        if(saldoCarteira < transacao.getValor()) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST, "Saldo insuficiente", 400);
        }
    }

    public DadosCarteiraResponse getDadosCarteira(UUID idColaborador) {
        logger.info("[CarteiraService.getDadosCarteira] Buscando dados da carteira para colaborador: {}", idColaborador);
        try {
            ZonedDateTime hojeZoned = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
            LocalDateTime startOfWeek = hojeZoned.toLocalDateTime();
            LocalDateTime endOfWeek = getEndOfWeek(startOfWeek);
            var colaborador = colaboradorService.getPorId(idColaborador);
            var carteira = repository.findByColaborador(colaborador).orElseThrow(() ->
                    new ResponseEntityException(HttpStatus.NOT_FOUND,
                            "Colaborador não encontrado", 400)
            );

            // Dados de desempenho semanal
            var carteiraResponse = new CarteiraResponse(carteira);
            var pacotesEntreguesEssaSemana = pacoteRepository.findAllPacotesEntreguesEssaSemana(startOfWeek, endOfWeek, colaborador);
            var totalGanhosSemanais = calcularValorPacotes((List<Pacote>) pacotesEntreguesEssaSemana, colaborador);
            var totalEntregasSemana = pacotesEntreguesEssaSemana.size();
            var pacotesTotaisSemana = pacoteRepository.findAllPacotesParaEntrega(startOfWeek, endOfWeek, colaborador);
            double porcentagemDesempenho = pacotesTotaisSemana.size() > 0
                    ? (totalEntregasSemana / (double) pacotesTotaisSemana.size()) * 100
                    : 0.0;
            var totalFaltaEntregar = pacoteRepository.findAllPacotesFaltaEntrega(startOfWeek, endOfWeek, colaborador);

            // Dados de desempenho diário
            var pacotesEntreguesHoje = pacoteRepository.findAllPacotesEntreguesHoje(colaborador);
            var totalEntreguesHoje = pacotesEntreguesHoje.size();
            var totalGanhosHoje = calcularValorPacotes(pacotesEntreguesHoje, colaborador);

            InformacoesAdicionaisResponse informacoesAdicionais = new InformacoesAdicionaisResponse(
                    totalEntreguesHoje, totalGanhosHoje
            );
            logger.info("[CarteiraService.getDadosCarteira] Dados da carteira obtidos com sucesso para colaborador: {}", idColaborador);
            return new DadosCarteiraResponse(
                    totalGanhosSemanais, totalEntregasSemana,
                    (int) porcentagemDesempenho,
                    carteiraResponse, informacoesAdicionais,
                    endOfWeek, startOfWeek, totalFaltaEntregar
            );
        } catch (Exception e) {
            logger.error("[CarteiraService.getDadosCarteira] Erro ao buscar dados da carteira para colaborador: {}", idColaborador, e);
            throw e;
        }
    }

    public LocalDateTime getEndOfWeek(LocalDateTime hoje) {
        DayOfWeek diaSemana = hoje.getDayOfWeek();

        if(diaSemana == DayOfWeek.SUNDAY) {
            return hoje;
        }

        return hoje.minusDays(7 - (DayOfWeek.SUNDAY.ordinal() - diaSemana.ordinal()));
    }

    private Double calcularValorPacotes(List<Pacote> pacotes, Colaborador colaborador) {
        logger.info("[CarteiraService.calcularValorPacotes] Calculando valor dos pacotes para colaborador: {}", colaborador.getIdColaborador());
        Double total = 0.0;
        for (var pacote : pacotes) {
            for (var pagamento : colaborador.getPagamentos()) {
                if (pagamento.getTipoPagamento().getCodigo() != 3 &&
                        pagamento.getTipoPagamento().getOrigemRemunerada() == pacote.getOrigem()) {
                    total += pagamento.getRemuneracao();
                }
            }
        }
        logger.info("[CarteiraService.calcularValorPacotes] Valor total calculado: {}", total);
        return total;
    }

    public void deletarPorId(UUID id) {
        logger.info("[CarteiraService.deletarPorId] Deletando carteira com id: {}", id);
        try {
            var carteira = getPorId(id);
            repository.delete(carteira);
            logger.info("[CarteiraService.deletarPorId] Carteira deletada com sucesso para id: {}", id);
        } catch (Exception e) {
            logger.error("[CarteiraService.deletarPorId] Erro ao deletar carteira com id: {}", id, e);
            throw e;
        }
    }

    public void atualizarSaldo(UUID id, AtualizacaoSaldo dto) {
        logger.info("[CarteiraService.atualizarSaldo] Atualizando saldo para carteira com id: {}", id);
        try {
            var carteira = getPorId(id);
            carteira.atualizarSaldo(dto.valor());
            repository.save(carteira);
            logger.info("[CarteiraService.atualizarSaldo] Saldo atualizado com sucesso para carteira com id: {}", id);
        } catch (Exception e) {
            logger.error("[CarteiraService.atualizarSaldo] Erro ao atualizar saldo para carteira com id: {}", id, e);
            throw e;
        }
    }

    public CarteiraResponse getCarteiraPorColaborador(UUID fkColaborador) {
        logger.info("[CarteiraService.getCarteiraPorColaborador] Buscando carteira para colaborador com id: {}", fkColaborador);
        try {
            var carteira = repository.findByColaborador(colaboradorService.getPorId(fkColaborador))
                    .orElseThrow(() -> new ResponseEntityException(HttpStatusCode.valueOf(404),
                            "Colaborador não possui carteira cadastrada", 404)
                    );
            logger.info("[CarteiraService.getCarteiraPorColaborador] Carteira encontrada para colaborador com id: {}", fkColaborador);
            return new CarteiraResponse(carteira);
        } catch (Exception e) {
            logger.error("[CarteiraService.getCarteiraPorColaborador] Erro ao buscar carteira para colaborador com id: {}", fkColaborador, e);
            throw e;
        }
    }
}
