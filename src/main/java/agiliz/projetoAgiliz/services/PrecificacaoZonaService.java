package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoResponseNoVendedor;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaCadastro;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaRequest;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaResponse;
import agiliz.projetoAgiliz.models.PrecificacaoZona;
import agiliz.projetoAgiliz.models.Vendedor;
import agiliz.projetoAgiliz.repositories.IPrecificacaoZona;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrecificacaoZonaService {

    private static final Logger logger = LoggerFactory.getLogger(PrecificacaoZonaService.class);
    private final IPrecificacaoZona repository;
    private final VendedorService vendedorService;

    public List<PrecificacaoZonaResponse> cadastrar(List<PrecificacaoZona> dto) {
        List<PrecificacaoZona> savedZonas = repository.saveAll(dto);
        List<PrecificacaoZonaResponse> response = savedZonas.stream()
                .map(savedZona -> new PrecificacaoZonaResponse(savedZona))
                .collect(Collectors.toList());
        logger.info("[PrecificacaoZonaService.cadastrar] Precificação de zona cadastrada com sucesso");
        return response;
    }

    public PrecificacaoZonaResponse cadastrar(PrecificacaoZonaRequest dto) {
        logger.info("[PrecificacaoZonaService.cadastrar] Início do cadastro de precificação de zona com dados: {}", dto);
        var precificacaoZona = new PrecificacaoZona();

        precificacaoZona.setVendedor(vendedorService.getPorId(dto.fkUnidade()));
        BeanUtils.copyProperties(dto, precificacaoZona);

        PrecificacaoZonaResponse response = new PrecificacaoZonaResponse(repository.save(precificacaoZona));
        logger.info("[PrecificacaoZonaService.cadastrar] Precificação de zona cadastrada com sucesso com ID {}", precificacaoZona.getIdPrecificacaoZona());
        return response;
    }

    public void deleteAllByVendedorId(UUID vendedorId) {
        repository.deleteByVendedorId(vendedorId);
    }

    public List<PrecificacaoResponseNoVendedor> cadastrar(List<PrecificacaoZonaCadastro> dto, Vendedor vendedor) {
        logger.info("[PrecificacaoZonaService.cadastrar] Início do cadastro de precificação de zona com dados: {}", dto);
        List<PrecificacaoZona> zonas = new ArrayList<>();

        for (var zonaDto : dto) {
            var precificacaoZona = new PrecificacaoZona();
            BeanUtils.copyProperties(zonaDto, precificacaoZona);
            precificacaoZona.setVendedor(vendedor);
            zonas.add(precificacaoZona);
        }
        List<PrecificacaoResponseNoVendedor> response = (repository.saveAll(zonas).stream()
                .map(PrecificacaoResponseNoVendedor::new).toList());

        logger.info("[PrecificacaoZonaService.cadastrar] Precificação de zona cadastrada com sucesso para o vendedor {}", vendedor.getNomeVendedor());
        return response;
    }

    public PrecificacaoResponseNoVendedor cadastrar(PrecificacaoResponseNoVendedor dto, UUID vendedor) {
        logger.info("[PrecificacaoZonaService.cadastrar] Início do cadastro de precificação de zona com dados: {}", dto);
        var vendedorEncontrado = vendedorService.getPorId(vendedor);
        var precificacaoZona = new PrecificacaoZona();
        BeanUtils.copyProperties(dto, precificacaoZona);
        precificacaoZona.setVendedor(vendedorEncontrado);
        var response = new PrecificacaoResponseNoVendedor(repository.save(precificacaoZona));
        logger.info("[PrecificacaoZonaService.cadastrar] Precificação de zona cadastrada com sucesso id: {}", response.idPrecificacaoZona());
        return response;
    }

    public Page<PrecificacaoZonaResponse> getPrecificacoesPaginadas(int pagina, int tamanho) {
        logger.info("[PrecificacaoZonaService.getPrecificacoesPaginadas] Consultando precificações paginadas: página {}, tamanho {}", pagina, tamanho);
        Page<PrecificacaoZonaResponse> precificacoesPaginadas = repository.findAllResponsePaginado(PageRequest.of(pagina, tamanho));
        logger.info("[PrecificacaoZonaService.getPrecificacoesPaginadas] Total de precificações encontradas: {}", precificacoesPaginadas.getTotalElements());
        return precificacoesPaginadas;
    }

    public PrecificacaoZona getPorId(UUID id) {
        logger.info("[PrecificacaoZonaService.getPorId] Consultando precificação de zona com ID {}", id);
        PrecificacaoZona precificacaoZona = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[PrecificacaoZonaService.getPorId] Precificação de zona com ID {} não encontrada", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Precificação de Zona não encontrada", 404);
                });
        logger.info("[PrecificacaoZonaService.getPorId] Precificação de zona com ID {} encontrada", id);
        return precificacaoZona;
    }

    public PrecificacaoZonaResponse getResponsePorId(UUID id) {
        logger.info("[PrecificacaoZonaService.getResponsePorId] Consultando resposta de precificação de zona com ID {}", id);
        PrecificacaoZonaResponse response = new PrecificacaoZonaResponse(getPorId(id));
        logger.info("[PrecificacaoZonaService.getResponsePorId] Resposta de precificação de zona com ID {} encontrada", id);
        return response;
    }

    public PrecificacaoZonaResponse alterar(UUID id, PrecificacaoZonaRequest dto) {
        logger.info("[PrecificacaoZonaService.alterar] Início da alteração da precificação de zona com ID {} e dados: {}", id, dto);
        var precificacaoZona = getPorId(id);
        BeanUtils.copyProperties(dto, precificacaoZona);
        PrecificacaoZonaResponse response = new PrecificacaoZonaResponse(repository.save(precificacaoZona));
        logger.info("[PrecificacaoZonaService.alterar] Precificação de zona com ID {} alterada com sucesso", id);
        return response;
    }

    //Me desculpa 😥
    public List<PrecificacaoZonaResponse> alterar(List<PrecificacaoResponseNoVendedor> dtoList, UUID idUnidade) {
        logger.info("[PrecificacaoZonaService.alterar] Início da alteração da precificação de zona com dados: {}", dtoList);
        List<PrecificacaoZonaResponse> responses = new ArrayList<>();

        dtoList.forEach(dto -> {
            if (dto.idPrecificacaoZona() == null) cadastrar(dto, idUnidade);
            var precificacaoZona = getPorId(dto.idPrecificacaoZona());
            BeanUtils.copyProperties(dto, precificacaoZona);
            precificacaoZona.setVendedor(vendedorService.getPorId(idUnidade));
            PrecificacaoZonaResponse response = new PrecificacaoZonaResponse(repository.save(precificacaoZona));
            responses.add(response);
            logger.info("[PrecificacaoZonaService.alterar] Precificação de zona com ID {} alterada com sucesso", dto.idPrecificacaoZona());
        });

        return responses;
    }

    public void deletarPorId(UUID id) {
        logger.info("[PrecificacaoZonaService.deletarPorId] Início da exclusão da precificação de zona com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[PrecificacaoZonaService.deletarPorId] Precificação de zona com ID {} excluída com sucesso", id);
    }
}
