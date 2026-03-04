package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.zona.ZonaRequest;
import agiliz.projetoAgiliz.dto.zona.ZonaResponse;
import agiliz.projetoAgiliz.dto.zona.ZonaResponsePut;
import agiliz.projetoAgiliz.models.Zona;
import agiliz.projetoAgiliz.repositories.IZonaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZonaService {
    private final IZonaRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(ZonaService.class);
    Pageable limit1 = PageRequest.of(0, 1);

    public String getZonaMaiorRetorno(LocalDateTime start, LocalDateTime end){
        var zonaMaiorRetorno = repository.findZonaMaiorEntrega(limit1, start, end);
        return zonaMaiorRetorno.isEmpty()?" ":zonaMaiorRetorno.get(0);
    }

    public int totalZonas(){
        return repository.findAll().size();
    }

    public int totalZonasNaoAtendidas(){
        return repository.countNaoAtendidas();
    }

    public String getZonaMenorRetorno(LocalDateTime start, LocalDateTime end){
        var zonaMenorRetorno = repository.findZonaMenorEntrega(limit1, start, end);
        return zonaMenorRetorno.isEmpty()?" ":zonaMenorRetorno.get(0);
    }

    public ZonaResponse cadastrar(ZonaRequest dto) {
        logger.info("[ZonaService.cadastrar] Cadastrando nova zona com dados: {}", dto);

        var zona = new Zona();
        BeanUtils.copyProperties(dto, zona);
        ZonaResponse response = new ZonaResponse(repository.save(zona));

        logger.info("[ZonaService.cadastrar] Zona cadastrada com sucesso");
        return response;
    }

    public Page<ZonaResponsePut> getZonasPaginadas(int pagina, int tamanho) {
        logger.info("[ZonaService.getZonasPaginadas] Consultando zonas paginadas: página {}, tamanho {}", pagina, tamanho);

        var pageable = PageRequest.of(pagina, tamanho, Sort.by("nomeZona"));
        Page<ZonaResponsePut> zonas = repository.findAllResponsePaginado(pageable);

        logger.info("[ZonaService.getZonasPaginadas] Zonas consultadas com sucesso: {} registros", zonas.getTotalElements());
        return zonas;
    }

    public ZonaResponse getResposePorId(UUID id) {
        logger.info("[ZonaService.getResposePorId] Consultando zona com ID {}", id);

        ZonaResponse response = new ZonaResponse(getPorId(id));
        logger.info("[ZonaService.getResposePorId] Zona consultada com sucesso, ID: {}", id);
        return response;
    }

    public Zona getPorId(UUID id) {
        logger.info("[ZonaService.getPorId] Consultando zona com ID {}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[ZonaService.getPorId] Zona com ID {} não encontrada", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Zona não encontrada", 404);
                });
    }

    public Zona getPorCep(String cep) {
        logger.info("[ZonaService.getPorCep] Consultando zona com CEP {}", cep);

        return repository.findByCep(Integer.valueOf(cep))
                .orElseThrow(() -> {
                    logger.error("[ZonaService.getPorCep] Zona com CEP {} não encontrada", cep);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Zona não encontrada", 404);
                });
    }

    public ZonaResponse alterar(UUID id, ZonaRequest dto) {
        logger.info("[ZonaService.alterar] Alterando zona com ID {} com dados: {}", id, dto);

        var zona = getPorId(id);
        BeanUtils.copyProperties(dto, zona);
        ZonaResponse response = new ZonaResponse(repository.save(zona));

        logger.info("[ZonaService.alterar] Zona alterada com sucesso, ID: {}", id);
        return response;
    }

    public void deletarPorId(UUID id) {
        logger.info("[ZonaService.deletarPorId] Deletando zona com ID {}", id);

        getPorId(id);
        repository.deleteById(id);

        logger.info("[ZonaService.deletarPorId] Zona com ID {} deletada com sucesso", id);
    }

    public List<Zona> getPorCeps(Set<String> ceps) {
        Set<Integer> cepsFormatados = ceps.stream().map(this::formatarCep).collect(Collectors.toSet());

        return repository.findAll()
                .stream()
                .filter(z ->
                    cepsFormatados.stream()
                            .anyMatch(cep ->
                                z.getLimiteInferiorCEP() <= cep &&
                                    z.getLimiteSuperiorCEP() >= cep
                            )
                )
                .toList();
    }

    public Integer formatarCep(String cep) {
        return Integer.parseInt(cep.substring(0, 5));
    }
}
