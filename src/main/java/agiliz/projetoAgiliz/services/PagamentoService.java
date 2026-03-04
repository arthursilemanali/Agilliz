package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoCadastroRequest;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoRequest;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoResponse;
import agiliz.projetoAgiliz.models.*;
import agiliz.projetoAgiliz.repositories.IPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagamentoService {
    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);
    private final IPagamentoRepository repository;
    private final ColaboradorService colaboradorService;
    private final TipoColaboradorService tipoColaboradorService;
    private final EmissaoPagamentoService emissaoService;
    private final DataService dataService;
    private final SetorService setorService;
    private final VigenciaPagamentoAutService vigenciaPagamentoService;
    private final TransacaoService transacaoService;

    public void deletarTodosPorId(UUID idColaborador) {
        logger.info("[PagamentoService.deletarTodoPorId] Consultando pagamentos paginados: deletando");
        deletarPorId(idColaborador);
    }

    public Page<PagamentoResponse> getPagamentosPaginados(int pagina, int tamanho) {
        logger.info("[PagamentoService.getPagamentosPaginados] Consultando pagamentos paginados: página {}, tamanho {}", pagina, tamanho);
        Page<PagamentoResponse> result = repository.findAllResponsePaginados(PageRequest.of(pagina, tamanho));
        logger.info("[PagamentoService.getPagamentosPaginados] Total de pagamentos encontrados: {}", result.getTotalElements());
        return result;
    }

    public Pagamento getPorId(UUID id) {
        logger.info("[PagamentoService.getPorId] Consultando pagamento com ID {}", id);
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[PagamentoService.getPorId] Pagamento com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Pagamento não encontrado", 404);
                });
        logger.info("[PagamentoService.getPorId] Pagamento com ID {} encontrado com sucesso", id);
        return pagamento;
    }

    public PagamentoResponse getResponsePorId(UUID id) {
        logger.info("[PagamentoService.getResponsePorId] Consultando resposta do pagamento com ID {}", id);
        PagamentoResponse response = new PagamentoResponse(getPorId(id));
        logger.info("[PagamentoService.getResponsePorId] Resposta do pagamento com ID {} consultada com sucesso", id);
        return response;
    }

    public PagamentoResponse cadastrar(PagamentoRequest dto) {
        logger.info("[PagamentoService.cadastrar] Início do cadastro do pagamento");
        var pagamento = new Pagamento(dto.tipoPagamento());
        BeanUtils.copyProperties(dto, pagamento);
        pagamento.setTipoColaborador(tipoColaboradorService.getPorId(dto.fkTipoColaborador()));
        pagamento.setColaborador(colaboradorService.getPorId(dto.fkFuncionario()));
        repository.save(pagamento);
        agendarPagamento(pagamento.getIdPagamento());
        logger.info("[PagamentoService.cadastrar] Pagamento cadastrado com sucesso: {}", pagamento);
        return new PagamentoResponse(pagamento);
    }

    public PagamentoResponse cadastrar(List<PagamentoCadastroRequest> dto) {
//        logger.info("[PagamentoService.cadastrar] Início do cadastro do pagamento");
//        List<Pagamento> pagamentos = new ArrayList<>();
//        for (var pagamento : dto){
//            var pagamentoEntity = new Pagamento();
//            BeanUtils.copyProperties(dto, pagamento);
//            pagamento.setTipoColaborador(tipoColaboradorService.getPorId(pagamento.getTipoColaborador().));
//        }
//        repository.save(pagamento);
//        agendarPagamento(pagamento.getIdPagamento());
//        logger.info("[PagamentoService.cadastrar] Pagamento cadastrado com sucesso: {}", pagamento);
//        return new PagamentoResponse(pagamento);
        return new PagamentoResponse(new Pagamento());
    }

    private void agendarPagamento(UUID idPagamento) {
        logger.info("[PagamentoService.agendarPagamento] Agendando pagamento com ID {}", idPagamento);
        // TODO Implementar agendamento de pagamento aqui
        logger.info("[PagamentoService.agendarPagamento] Pagamento com ID {} agendado com sucesso", idPagamento);
    }

    public Pagamento findPagamentoByColaboradorAndTipo(Colaborador colaborador, int tipo) {
        return repository.findByColaboradorAndTipo(colaborador, tipo)
                .orElseThrow(() ->
                        new ResponseEntityException(HttpStatus.NOT_FOUND, "Nenhum pagamento encontrado para esse tipo", 404)
                );
    }

    public void deletarPorId(UUID id) {
        logger.info("[PagamentoService.deletarPorId] Início da exclusão do pagamento com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[PagamentoService.deletarPorId] Pagamento com ID {} excluído com sucesso", id);
    }

    public List<Pagamento> getSalarios() {
        logger.info("[PagamentoService.getSalarios] Consultando pagamentos do tipo salário");
        List<Pagamento> pagamentos = repository.findSalarios();

        logger.info("[PagamentoService.getPagamentosPaginados] Total de pagamentos encontrados: {}", pagamentos.size());
        return pagamentos;
    }

    public void cadastrarSalarios() {
        if (dataService.isNotQuintoDiaUtil(LocalDate.now())) {
            return;
        }

        var salarios = getSalarios();

        if (salarios.isEmpty()) {
            return;
        }

        logger.info("[PagamentoService.cadastrarSalarios] Realizando operação de cadastro de salário");
        emissaoService.cadastrar(salarios);
    }

    public void bonificarLideres() {
        LocalDate diaAtual = LocalDate.now();

        if(dataService.isNotQuintoDiaUtil(diaAtual)) {
            return;
        }

        logger.info("[PagamentoService.bonificarLideres] Realizando bonificação de líderes de setores");

        LocalDate vigenciaInicio = dataService.getQuintoDiaUtil(diaAtual.minusMonths(1));
        emissaoService.cadastrarBonificacao(setorService.getBonificacaoLideres(vigenciaInicio, diaAtual));
    }

    public void realizarPagamentoAut() {
        if(LocalDate.now().getDayOfWeek() != vigenciaPagamentoService.getVigenciaDiaSemana()) {
            return;
        }

        logger.info("[PagamentoService.realizarPagamentoAut] Realizando operação de pagamento aut");

        transacaoService.realizarTransacoesAut(colaboradorService.getColaboradoresComSaldo());
    }
}