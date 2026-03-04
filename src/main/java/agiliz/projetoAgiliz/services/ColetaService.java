package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.coleta.*;
import agiliz.projetoAgiliz.dto.dashColetas.ColetasEmAndamento;
import agiliz.projetoAgiliz.dto.dashColetas.ColetasSLA;
import agiliz.projetoAgiliz.enums.StatusColeta;
import agiliz.projetoAgiliz.enums.StatusPacote;
import agiliz.projetoAgiliz.models.Coleta;
import agiliz.projetoAgiliz.models.Coletor;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.repositories.ColetaRepositoryCustom;
import agiliz.projetoAgiliz.repositories.IColetaRepository;
import agiliz.projetoAgiliz.repositories.IColetorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ColetaService {
    private static final Logger logger = LoggerFactory.getLogger(ColetaService.class);
    private final IColetaRepository repository;
    private final VendedorService vendedorService;
    private final ZonaService zonaService;
    private final ColaboradorService colaboradorService;
    private final IColetorRepository coletorRepository;
    private final ColetaRepositoryCustom coletaRepositoryCustom;

    public List<ColetaResponse> getDispiniveisConferencia() {
        return repository.findAllDisponiveisConferencia().stream()
                .map(ColetaResponse::new).toList();
    }

    public List<ColetaResponse> getColetasNaoAtribuidas() {
        return repository.findAllColetasNaoAtribuidas().stream()
                .map(ColetaResponse::new).toList();
    }

    public List<ColetaResponse> findByFilters(String campo, String valor) {
        if (Objects.equals(campo, "") || Objects.equals(valor, "")) {
            return repository.findAll().stream().map(ColetaResponse::new).toList();
        }
        Map<String, String> filtros = new HashMap<>();
        filtros.put(campo, valor);
        return coletaRepositoryCustom.findByFilters(filtros)
                .stream()
                .map(ColetaResponse::new)
                .toList();
    }

    public Long countColetas() {
        return repository.count();
    }

    public List<Coleta> listarTodas() {
        return repository.findAll();
    }

    public void desassociarColaborador(String idColeta, String idColaborador) {
        var coletor = coletorRepository
                .getColetorPorIdDeFuncionarioEIdColeta(
                        UUID.fromString(idColaborador),
                        UUID.fromString(idColeta))
                .orElseThrow(() -> new ResponseEntityException(
                        HttpStatus.NOT_FOUND,
                        "Colaborador não encontrado para essa coleta",
                        404));
        coletorRepository.deleteById(coletor.getIdColetor());
    }

    public List<ColetaResponse> getColetasAtribuidas(String idColaborador) {
        return repository.getColetasAtribuidasPorId(UUID.fromString(idColaborador))
                .stream().map(ColetaResponse::new).toList();
    }

    public Boolean isExpedido(String idColeta) {
        return repository.getPacotesFromColeta(UUID.fromString(idColeta))
                .stream()
                .allMatch(pacote -> pacote.getStatus() == StatusPacote.A_CAMINHO);
    }

    public List<ColetasEmAndamento> getColetasEmEspera() {
        return repository.getColetasEmAndamento().stream().map(ColetasEmAndamento::new).toList();
    }

    public List<ColetasSLA> getSlaColetas(LocalDateTime start, LocalDateTime end) {
        return repository.getSlaColetas(start, end).stream().map(ColetasSLA::new).toList();
    }

    public void cadastrarML(ColetaMLRequest coletaMLRequest) {
        var coleta = new Coleta();
        var vendedor = vendedorService.getPorId(UUID.fromString(coletaMLRequest.fkVendedor()));
        coleta.setRomaneio(coletaMLRequest.romaneio());
        coleta.setStatusColeta(1);
        coleta.setVendedor(vendedorService.getPorId(UUID.fromString(coletaMLRequest.fkVendedor())));
        coleta.setPacotes(coletaMLRequest.pacotes().stream()
                .map(p -> new Pacote(p,
                        vendedor, zonaService.getPorCep(p.destination().shipping_address().zip_code()), coleta)).toList());

        repository.save(coleta);
    }

    public List<ColetaResponse> getColetasFinalizadasByIdColaborador(String idColaborador) {
        return repository.getColetasFinalizadasByIdColaborador(UUID.fromString(idColaborador))
                .stream().map(ColetaResponse::new).toList();
    }

    public void inserir(Coleta coleta) {
        repository.save(coleta);
    }

    public List<ColetaPacotes> getPacoteColetas(List<String> idColeta) {
        return repository.findAllById(idColeta.stream().map(UUID::fromString).toList())
                .stream().map(ColetaPacotes::new).toList();
    }

    @Transactional
    public void associarColaborador(String idColeta, String idColaborador, String token) {
//        if (!temRoleAdmin(token) && !isMotoboyPodeAssociar(token, idColeta)) {
//            throw new ResponseEntityException(HttpStatus.FORBIDDEN,
//                    "Você não tem permissão para realizar essa operação", 403);
//        }
        var colaborador = colaboradorService.getPorId(UUID.fromString(idColaborador));
        var coleta = getPorId(UUID.fromString(idColeta));

        if (!temRoleAdmin(token) && (!coleta.getColetores().isEmpty()
                && coleta.getStatusColeta() != StatusColeta.PRONTA_PARA_CONFERENCIA)) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST,
                    "Essa coleta já possui um colaborador ativo", 400);
        }

        var coletor = new Coletor();

        if (coleta.getStatusColeta().equals(StatusColeta.PRONTA_PARA_CONFERENCIA)) {
            coletor.setConferente(true);
        }

        coletor.setColaborador(colaborador);
        coletor.setColeta(coleta);
        coletor.setDataColeta(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDate());
        coletorRepository.save(coletor);
    }

    private boolean temRoleAdmin(String token) {
        List<String> roles = colaboradorService.extractRoles(token);
        return roles.contains("ROLE_ADMIN");
    }

    private boolean isMotoboyPodeAssociar(String token, String idColeta) {
        List<String> roles = colaboradorService.extractRoles(token);
        boolean isMotoboy = roles.contains("MOTOBOY");

        if (isMotoboy) {
            return false;
        }

        var coleta = getPorId(UUID.fromString(idColeta));
        return coleta.getColetores().isEmpty();
    }


    public List<ColetaPacotes> getAllConcluidas() {
        logger.info("[ColetaService.getAllConcluidas] Inicio da getAllConcluidas");
        List<Coleta> coletas = repository.findAllColetasConcluidas();
        logger.info("[ColetaService.getAllConcluidas] Número de coletas retornadas: {}", coletas.size());
        return coletas.stream().map(ColetaPacotes::new).toList();
    }

    public Map<String, String> getCampos() {
        return ColetaCampos.camposColeta();
    }

    public void alterarStatus(String idColeta, Integer codigo) {
        var coleta = getPorId(UUID.fromString(idColeta));
        StatusColeta.valueOf(codigo);
        coleta.setStatusColeta(codigo);
        repository.save(coleta);
    }

    public ColetaResponse inserir(ColetaPostRequest dto) {
        logger.info("[ColetaService.inserir] Início da inserção da coleta");
        var coleta = new Coleta(dto.romaneio());
        coleta.setVendedor(vendedorService.getPorId(dto.vendedor()));
        var response = new ColetaResponse(repository.save(coleta));
        // jonnas eu coloquei o getId pq tava dando stackOverflow rs 👻
        logger.info("[ColetaService.inserir] Coleta inserida com sucesso: {}", coleta.getIdColeta());
        return response;
    }

    public void atualizarStatus(Coleta coleta, int conferencia) {
        StatusColeta statusAntigo = coleta.getStatusColeta();

        coleta.setStatusColeta(2);

        if (conferencia == 0) coleta.setStatusColeta(1);
        if (coleta.getRomaneio() == conferencia) coleta.setStatusColeta(3);

        if (coleta.getStatusColeta() != statusAntigo) repository.save(coleta);
    }

    public List<ColetaResponse> getColetasResponse() {
        logger.info("[ColetaService.getColetasResponse] Início da consulta de coletas");
        var coletas = repository.findAllResponse();
        logger.info("[ColetaService.getColetasResponse] Total de coletas encontradas {}", coletas.size());
        return coletas;
    }

    public Coleta getPorId(UUID id) {
        logger.info("[ColetaService.getPorId] Consultando coleta com ID {}", id);
        var coleta = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[ColetaService.getPorId] Coleta com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Coleta não encontrada", 404);
                });
        logger.info("[ColetaService.getPorId] Coleta com ID {} encontrada com sucesso", id);
        return coleta;
    }

    public ColetaResponse getResponsePorId(UUID id) {
        logger.info("[ColetaService.getResponsePorId] Consultando coleta com ID {}", id);
        var response = new ColetaResponse(getPorId(id));
        logger.info("[ColetaService.getResponsePorId] Coleta com ID {} encontrada com sucesso", id);
        return response;
    }

    public ColetaResponse alterar(UUID id, ColetaPutRequest dto) {
        logger.info("[ColetaService.alterar] Início da alteração da coleta com ID {}", id);
        var coleta = getPorId(id);
        BeanUtils.copyProperties(dto, coleta);
        var response = new ColetaResponse(repository.save(coleta));
        logger.info("[ColetaService.alterar] Coleta alterada com sucesso: {}", response);
        return response;
    }

    public void deletarPorId(UUID id) {
        logger.info("[ColetaService.deletarPorId] Início da exclusão da coleta com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[ColetaService.deletarPorId] Coleta com ID {} excluído com sucesso", id);
    }

    public List<ColetaResponse> getColetasAbertas() {
        logger.info("[ColetaService.getColetasAbertas] Consultando coletas abertas");
        var coletas = repository.findColetasAbertas();
        logger.info("[ColetaService.getColetasAbertas] Total de coletas encontradas {}", coletas.size());
        return coletas;
    }
}
