package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorRequest;
import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorResponse;
import agiliz.projetoAgiliz.models.TipoColaborador;
import agiliz.projetoAgiliz.repositories.ITipoColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TipoColaboradorService {

    private final ITipoColaboradorRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(TipoColaboradorService.class);

    public TipoColaborador getPorId(UUID id) {
        logger.info("[TipoColaboradorService.getPorId] Consultando tipo de colaborador com ID {}", id);
        TipoColaborador tipoColaborador = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[TipoColaboradorService.getPorId] Tipo de colaborador com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatusCode.valueOf(404), "Tipo de colaborador com o id listado não existe", 404);
                });
        logger.info("[TipoColaboradorService.getPorId] Tipo de colaborador com ID {} encontrado", id);
        return tipoColaborador;
    }

    public TipoColaboradorResponse inserir(TipoColaboradorRequest dto) {
        logger.info("[TipoColaboradorService.inserir] Início da inserção do tipo de colaborador com dados: {}", dto);
        var tipoColaborador = new TipoColaborador();
        BeanUtils.copyProperties(dto, tipoColaborador);
        logger.info("[TipoColaboradorService.inserir] Tipo de colaborador inserido com sucesso com ID {}", tipoColaborador.getIdTipoColaborador());
        return new TipoColaboradorResponse(repository.save(tipoColaborador));
    }

    public TipoColaborador inserirByColaborador(TipoColaboradorRequest dto) {
        logger.info("[TipoColaboradorService.inserir] Início da inserção do tipo de colaborador com dados: {}", dto);
        var tipoColaborador = new TipoColaborador();
        BeanUtils.copyProperties(dto, tipoColaborador);
        logger.info("[TipoColaboradorService.inserir] Tipo de colaborador inserido com sucesso com ID {}", tipoColaborador.getIdTipoColaborador());
        return repository.save(tipoColaborador);
    }


    public List<TipoColaboradorResponse> listar() {
        logger.info("[TipoColaboradorService.listar] Consultando lista de tipos de colaborador");
        List<TipoColaboradorResponse> tipoColaboradores = repository.findAllTipoColaboradorResponse();
        logger.info("[TipoColaboradorService.listar] Total de tipos de colaborador encontrados: {}", tipoColaboradores.size());
        return tipoColaboradores;
    }
}
