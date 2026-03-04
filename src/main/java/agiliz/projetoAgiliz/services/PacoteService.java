package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.clients.MlClient;
import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.PacotePorcentagemDTO;
import agiliz.projetoAgiliz.dto.carteira.AtualizacaoSaldo;
import agiliz.projetoAgiliz.dto.colaborador.AssociarColaboradorPacote;
import agiliz.projetoAgiliz.dto.dashColetas.ColetasPorTempo;
import agiliz.projetoAgiliz.dto.dashColetas.ZonaRanking;
import agiliz.projetoAgiliz.dto.dashEntregas.RankingEntregasDTO;
import agiliz.projetoAgiliz.dto.pacote.ColetasResponseEmEspera;
import agiliz.projetoAgiliz.dto.pacote.PacoteRequest;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.dto.pacote.PacotesColetaRequest;
import agiliz.projetoAgiliz.dto.roteirizacao.RegistroSaida;
import agiliz.projetoAgiliz.dto.roteirizacao.Roteirizacao;
import agiliz.projetoAgiliz.enums.StatusPacote;
import agiliz.projetoAgiliz.enums.TipoPacote;
import agiliz.projetoAgiliz.models.Destinatario;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.Pagamento;
import agiliz.projetoAgiliz.models.Zona;
import agiliz.projetoAgiliz.repositories.IPacoteRepository;
import agiliz.projetoAgiliz.repositories.IPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacoteService {
    private static final Logger logger = LoggerFactory.getLogger(PacoteService.class);
    private final IPacoteRepository repository;
    private final ZonaService zonaService;
    private final DestinatarioService destinatarioService;
    private final ColaboradorService colaboradorService;
    private final VendedorService vendedorService;
    private final ColetaService coletaService;
    private final IPagamentoRepository pagamentoRepository;
    private final CarteiraService carteiraService;
    private final RoteirizacaoService roteirizacaoService;
    private final FirebaseService firebaseService;
    private final MlClient mlClient;
    private final SetorService setorService;

    Pageable firstResult = PageRequest.of(0, 1);

    public void entregarPacote(String idPacote) {
        var pacote = getPorId(UUID.fromString(idPacote));
        switch (pacote.getOrigem()) {
            case MERCADO_LIVRE:
                logger.info("[PacoteService.entregarPacote] Entregando pacote do Mercado Livre");
                realizarEntregaML(pacote);
                break;
            case SHOPEE:
                logger.info("[PacoteService.entregarPacote] Entregando pacote da Shopee");
                realizarEntregaShopee(pacote);
                break;
            case OUTROS:
                logger.info("[PacoteService.entregarPacote] Entregando pacote de outra origem");
                realizarEntregaOutro(pacote);
                break;
            default:
                logger.error("[PacoteService.entregarPacote] Origem do pacote desconhecida: {}", pacote.getOrigem());
                throw new IllegalArgumentException("Origem do pacote desconhecida: " + pacote.getOrigem());
        }
    }

    public void realizarEntregaOutro(Pacote pacote){
        //TODO
    }

    public void realizarEntregaShopee(Pacote pacote){
        //TODO
    }

    public void realizarEntregaML(Pacote pacote){
        var response = mlClient.getStatusPacote(pacote.getVendedor().getId_ecommerce().toString(), Integer.valueOf(pacote.getIdEcommerce())).getBody();
        if(response!=null){
            pacote.setStatus(StatusPacote.valueOf(response.status()).getCodigo());
        }
    }

    public Boolean isCompleted(String idPacote) {
        return coletaService.isExpedido(idPacote);
    }

    public List<PacoteResponse> getPacotesParaEntregarPorIdColaborador(String idColaborador) {
        logger.info("[PacoteService.getPacotesParaEntregarPorIdColaborador] Iniciando busca de pacotes para o colaborador com ID: {}", idColaborador);

        List<PacoteResponse> pacotesResponse;
        try {
            UUID colaboradorUUID = UUID.fromString(idColaborador);
            logger.debug("[PacoteService.getPacotesParaEntregarPorIdColaborador] ID do colaborador convertido para UUID: {}", colaboradorUUID);

            pacotesResponse = repository.getPacotesParaEntregarPorIdColaborador(colaboradorUUID)
                    .stream()
                    .map(PacoteResponse::new)
                    .toList();

            logger.info("[PacoteService.getPacotesParaEntregarPorIdColaborador] {} pacotes encontrados para o colaborador com ID: {}", pacotesResponse.size(), idColaborador);
        } catch (IllegalArgumentException e) {
            logger.error("[PacoteService.getPacotesParaEntregarPorIdColaborador] ID do colaborador inválido: {}", idColaborador, e);
            throw e;
        } catch (Exception e) {
            logger.error("[PacoteService.getPacotesParaEntregarPorIdColaborador] Erro ao buscar pacotes para o colaborador com ID: {}", idColaborador, e);
            throw e;
        }

        logger.debug("[PacoteService.getPacotesParaEntregarPorIdColaborador] Pacotes retornados: {}", pacotesResponse);
        return pacotesResponse;
    }

    public List<PacoteResponse> getByEcommerceId(List<Integer> idEcommerce, String idColaborador) {
        logger.info("[PacoteService.getByEcommerceId] Consultando pacotes para o e-commerce ID {}", idEcommerce);
        var pacotes = repository.findAllByIdEcommerce(idEcommerce);
        if (pacotes.isEmpty()) {
            logger.warn("[PacoteService.getByEcommerceId] Nenhum pacote encontrado para o e-commerce ID {}", idEcommerce);
            throw new ResponseEntityException(
                    HttpStatus.NO_CONTENT,
                    "Pacote não encontrado para o id especificado",
                    HttpStatus.NO_CONTENT.value());
        }
        var colaborador = colaboradorService.getPorId(UUID.fromString(idColaborador));
        pacotes.forEach(p -> {
                    p.setColaborador(colaborador);
                    p.setStatus(6);
                }
        );
        repository.saveAll(pacotes);
        logger.info("[PacoteService.getByEcommerceId] Total de pacotes encontrados: {}", pacotes.size());
        return pacotes.stream().map(PacoteResponse::new).toList();
    }

    public void associarColaboradorPacote(AssociarColaboradorPacote colaboradorPacote) {
        logger.info("[PacoteService.associarColaboradorPacote] Associando colaborador com ID {}", colaboradorPacote.idColaborador());
        var pacotes = repository.findAllById(colaboradorPacote
                .pacotes()
                .stream()
                .map(UUID::fromString)
                .toList());
        var colaborador = colaboradorService.getPorId(UUID.fromString(colaboradorPacote.idColaborador()));

        if (!pacotes.isEmpty()) {
            pacotes.stream()
                    .filter(p -> p.getTipo().getCodigo() == 1 && p.getColaborador() == null)
                    .forEach(p -> {
                        p.setColaborador(colaborador);
                        p.setTipo(1);
                        alterarStatus(p.getIdPacote(), 6, colaborador.getIdColaborador());

                        logger.info("[PacoteService.associarColaboradorPacote] Pacote ID {} associado ao colaborador ID {}",
                                p.getIdPacote(), colaboradorPacote.idColaborador());
                    });
        }

        repository.saveAll(pacotes);
        String mensagem = new StringBuilder()
                .append("Olá, ")
                .append(colaborador.getNomeColaborador())
                .append("! ")
                .append(pacotes.size())
                .append(pacotes.size() == 1 ? " pacote" : " pacotes")
                .append(pacotes.size() == 1 ? " foi atribuído" : " foram atribuídos")
                .append(" para você.")
                .append(" Verifique os detalhes no aplicativo.")
                .toString();

        firebaseService.notificarMotoboy(colaborador.getIdColaborador().toString(), mensagem, "Nova atribuição de pacote");
        logger.info("[PacoteService.associarColaboradorPacote] Associação de pacotes concluída");
    }

    public List<ColetasResponseEmEspera> findAllPacotesEmEspera() {
        logger.info("[PacoteService.findAllPacotesEmEspera] Consultando pacotes em espera");
        return vendedorService.getAll()
                .stream().map(ColetasResponseEmEspera::new)
                .toList();
    }

    public List<Pacote> findAllByIds(List<UUID> ids) {
        logger.info("[PacoteService.findAllByIds] Consultando pacotes por IDs");
        return repository.findAllById(ids);
    }

    public List<Pacote> insertAllPacotes(List<Pacote> pacotes) {
        logger.info("[PacoteService.insertAllPacotes] Inserindo pacotes");
        return repository.saveAll(pacotes);
    }

    public List<Pacote> associarColaborador(Set<UUID> idsPacote, UUID idColaborador) {
        var pacotes = repository.findAllById(idsPacote);

        var colaborador = colaboradorService.getPorId(idColaborador);

        pacotes.forEach(pacote -> pacote.setColaborador(colaborador));

        return repository.saveAll(pacotes);
    }

    public List<Pacote> getPacotesParaPagar(Pagamento pagamento) {
        return repository.findPackagesForPayment(
                pagamento.getColaborador(),
                pagamento.getTipoPagamento().getOrigemRemunerada().getCodigo()
        );
    }

    public Page<PacoteResponse> getPacotesColeta(int pagina, int tamanho) {
        return repository.findAllPacoteColetas(PageRequest.of(pagina, tamanho));
    }

    public void alterarStatus(UUID id, int statusPacote, UUID idColaborador) {
        var pacote = getPorId(id);
        pacote.setStatus(statusPacote);

        if (pacote.getTipo() == TipoPacote.ENTREGA) {
            if (pacote.getStatus() == StatusPacote.A_CAMINHO) {
                // TODO Jogar pro ms de zap
                if (coletaService.isExpedido(id.toString())) {
                    var coleta = coletaService.getPorId(id);
                    coleta.setStatusColeta(5);
                    coletaService.inserir(coleta);
                }
            }

            if (pacote.getStatus() == StatusPacote.CONCLUIDO) {
                ZonedDateTime zonedDateTimeSP = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
                pacote.setDataEntrega(zonedDateTimeSP.toLocalDateTime());

                var pagamento = pagamentoRepository.findByColaboradorAndTipo
                        (pacote.getColaborador(), pacote.getOrigem().getCodigo()).orElseThrow(() ->
                        new ResponseEntityException(HttpStatus.NOT_FOUND
                                , "Nenhum pagamento encontrado para esse tipo", 404));

                carteiraService.atualizarSaldo
                        (pacote.getColaborador().getCarteira().getIdCarteira(),
                                new AtualizacaoSaldo(pagamento.getRemuneracao()));
            }
        }

        if (pacote.getTipo() == TipoPacote.COLETA && pacote.getStatus() == StatusPacote.CONCLUIDO) {
            pacote.setDataColeta(LocalDateTime.now());

            var pacoteEntrega = new Pacote();

            BeanUtils.copyProperties(pacote, pacoteEntrega);
            pacoteEntrega.setTipo(TipoPacote.ENTREGA.getCodigo());
            pacoteEntrega.setStatus(StatusPacote.EM_ESPERA.getCodigo());

            if (idColaborador != null)
                pacoteEntrega.setColaborador(colaboradorService.getPorId(idColaborador));

            repository.save(pacoteEntrega);
        }

        repository.save(pacote);
    }

    // Medida provisória pra inserir um pacotes do tipo (Enum) coleta e associar a uma (Model) Coleta depois quando o sistema estiver integrado, não vai precisar inserir, só associar mesmo
    public List<PacoteResponse> inserirColeta(PacotesColetaRequest dto) {
        var coleta = coletaService.getPorId(dto.fkColeta());
        var colaborador = colaboradorService.getPorId(dto.fkColaborador());

        var destinatarioIds = dto.pacotes().stream()
                .map(PacoteRequest::fkDestinatario).collect(Collectors.toSet());

        var destinatarios = destinatarioService.buscarPorIds(destinatarioIds);

        var ceps = destinatarios.stream()
                .map(Destinatario::getCep)
                .collect(Collectors.toSet());

        var zonas = zonaService.getPorCeps(ceps);

        Map<UUID, Destinatario> destinatarioMap = destinatarios.stream()
                .collect(Collectors.toMap(Destinatario::getIdDestinatario, destinatario -> destinatario));

        Map<UUID, Zona> zonaMap = new HashMap<>();

        for (UUID idDestinatario : destinatarioMap.keySet()) {
            var cep = zonaService.formatarCep(destinatarioMap.get(idDestinatario).getCep());

            var zona = zonas.stream()
                    .filter(z -> z.getLimiteInferiorCEP() <= cep && z.getLimiteSuperiorCEP() >= cep)
                    .findAny()
                    .get();

            zonaMap.put(idDestinatario, zona);
        }

        List<Pacote> pacotes = dto.pacotes().stream()
                .map(
                        pctRequest -> {
                            var p = new Pacote();
                            p.setColeta(coleta);
                            p.setVendedor(coleta.getVendedor());
                            p.setColaborador(colaborador);
                            p.setDestinatario(destinatarioMap.get(pctRequest.fkDestinatario()));
                            p.setZona(zonaMap.get(pctRequest.fkDestinatario()));
                            BeanUtils.copyProperties(pctRequest, p);
                            return p;
                        }
                )
                .toList();

        return repository.saveAll(pacotes).stream().map(PacoteResponse::new).toList();
    }

    public Page<PacoteResponse> getPacotesResponsePaginados(int pagina, int tamanho) {
        return repository.findPacotesResponse(PageRequest.of(pagina, tamanho));
    }


    public PacoteResponse getResponsePorId(UUID id) {
        return new PacoteResponse(getPorId(id));
    }

    public void deletarPorId(UUID id) {
        getPorId(id);
        repository.deleteById(id);
    }

    public List<PacotePorcentagemDTO> listarPacotesPorcentagem() {
        return repository.listarPorcentagem();
    }

    public PacoteResponse inserir(PacoteRequest dto) {
        logger.info("[PacoteService.inserir] Início da inserção do pacote");
        var pacote = new Pacote();
        associarRelacionamentos(dto, pacote);
        BeanUtils.copyProperties(dto, pacote);
        var response = new PacoteResponse(repository.save(pacote));
        logger.info("[PacoteService.inserir] Pacote inserido com sucesso: {}", response);
        return response;
    }

    public List<ZonaRanking> getZonasRankeadas(LocalDateTime start, LocalDateTime end, int numeroEntregas) {
        logger.info("[PacoteService.getZonasRankeadas] Consultando zonas rankeadas entre {} e {} com número de entregas {}", start, end, numeroEntregas);
        return repository.findTop3ZonasRanking(start, end, numeroEntregas);
    }

    public List<Pacote> getAllPacoteStatusOnly(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getAllPacoteStatusOnly] Consultando pacotes com status entre {} e {}", start, end);
        return repository.findAllPacoteStatusOnly(start, end);
    }

    public List<ColetasPorTempo> getColetasPorTempo(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getColetasPorTempo] Consultando coletas por tempo entre {} e {}", start, end);
        return repository.findColetasPorTempo(start, end);
    }

    public Integer countDevolvidas(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.countDevolvidas] Contando pacotes devolvidos entre {} e {}", start, end);
        return repository.countDevolvidas(start, end);
    }

    public long getQuantidadeColetasRealizadas(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getQuantidadeColetasRealizadas] Contando coletas realizadas entre {} e {}", start, end);
        return repository.countColetasRealizadas(start, end).size();
    }

    public long getQuantidadeColetasCanceladas(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getQuantidadeColetasCanceladas] Contando coletas canceladas entre {} e {}", start, end);
        return repository.countColetasCanceladas(start, end).size();
    }

    public String getNomeClienteMaiorColeta(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getNomeClienteMaiorColeta] Buscando cliente com maior coleta entre {} e {}", start, end);
        var clienteMaiorColeta = repository.findClienteMaiorColeta(firstResult, start, end);
        return clienteMaiorColeta.isEmpty() ? "" : clienteMaiorColeta.get(0);
    }

    public String getNomeClienteMenorColeta(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.getNomeClienteMenorColeta] Buscando cliente com menor coleta entre {} e {}", start, end);
        var clienteMenorColeta = repository.findClienteMenorColeta(firstResult, start, end);
        return clienteMenorColeta.isEmpty() ? "" : clienteMenorColeta.get(0);
    }

    public Pacote atualizar(PacoteRequest dto, UUID id) {
        logger.info("[PacoteService.atualizar] Atualizando pacote com ID {}", id);
        var pacote = getPorId(id);

        associarRelacionamentos(dto, pacote);
        BeanUtils.copyProperties(dto, pacote);

        logger.info("[PacoteService.atualizar] Pacote atualizado com sucesso: {}", pacote);
        return repository.save(pacote);
    }

    public List<RankingEntregasDTO> listarRankingEntregas(LocalDateTime start, LocalDateTime end) {
        logger.info("[PacoteService.listarRankingEntregas] Listando ranking de entregas entre {} e {}", start, end);
        Pageable limit3 = PageRequest.of(0, 3);
        return repository.listarRankingEntregas(limit3, start, end);
    }

    public Pacote getPorId(UUID id) {
        logger.info("[PacoteService.getPorId] Consultando pacote com ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[PacoteService.getPorId] Pacote não encontrado com ID {}", id);
                    return new ResponseEntityException(
                            HttpStatus.NOT_FOUND,
                            "Pacote não encontrado com ID " + id,
                            HttpStatus.NOT_FOUND.value());
                });
    }

    public Roteirizacao roteirizarPacotes(RegistroSaida dto) {
        var pacotes = repository.findAllById(dto.idsPacote());

        var response = roteirizacaoService.getRoteirizacao(pacotes, dto.inicio(), dto.fim());
        var pctMap = pacotes.stream().collect(Collectors.toMap(Pacote::getIdPacote, p -> p));

        return new Roteirizacao(response, pctMap);
    }

    private void associarRelacionamentos(PacoteRequest dto, Pacote pacote) {
        pacote.setDestinatario(destinatarioService.getPorId(dto.fkDestinatario()));
        pacote.setZona(zonaService.getPorCep(pacote.getDestinatario().getCep()));
        pacote.setVendedor(vendedorService.getPorId(dto.fkUnidade()));

        setorService.associarSetor(pacote);

        if (dto.fkFuncionario() != null)
            pacote.setColaborador(colaboradorService.getPorId(dto.fkFuncionario()));
    }
}
