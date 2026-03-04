package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.manifesto.ManifestoRequest;
import agiliz.projetoAgiliz.dto.manifesto.ManifestoResponse;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.enums.TipoManifesto;
import agiliz.projetoAgiliz.models.Manifesto;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.RegistroManifesto;
import agiliz.projetoAgiliz.repositories.IManifestoRepository;
import agiliz.projetoAgiliz.repositories.IRegistroManifestoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ManifestoService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestoService.class);
    @Autowired
    private IManifestoRepository repository;
    @Autowired
    private ColetaService coletaService;
    @Autowired
    private IRegistroManifestoRepository registroManifestoRepository;

    public List<ManifestoResponse> cadastrar(List<ManifestoRequest> dtos) {
        logger.info("[ManifestoService.cadastrar] Início do cadastro de {} manifestos", dtos.size());
        List<ManifestoResponse> responses = new ArrayList<>();
        dtos.forEach(dto -> {
            Manifesto manifesto = new Manifesto(dto.tipo());
            Manifesto manifestoSalvo = repository.save(manifesto);
            if (dto.idColeta() != null && dto.tipo() == 1) {
                var coleta = coletaService.getPorId(dto.idColeta());
                var registrosManifesto = coleta.getPacotes().stream()
                        .map(pacote -> new RegistroManifesto(manifestoSalvo, pacote))
                        .toList();
                registroManifestoRepository.saveAll(registrosManifesto);
            }
            var pacotes = registroManifestoRepository.findByManifestoId(manifesto).stream().map(
                    registroManifesto -> new PacoteResponse(registroManifesto.getPacote())
            ).toList();

            responses.add(new ManifestoResponse(manifestoSalvo, pacotes));
            logger.info("[ManifestoService.cadastrar] Manifesto cadastrado com sucesso com ID {}", manifestoSalvo.getIdManifesto());
        });
        return responses;
    }
    public List<ManifestoResponse> getManifestosResponse() {
        logger.info("[ManifestoService.getManifestosResponse] Consultando todos os manifestos");
        List<ManifestoResponse> response = repository.findAllResponse();
        logger.info("[ManifestoService.getManifestosResponse] Total de manifestos consultados: {}", response.size());
        return response;
    }

    public Manifesto getPorId(UUID id) {
        logger.info("[ManifestoService.getPorId] Consultando manifesto com ID {}", id);
        Manifesto manifesto = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[ManifestoService.getPorId] Manifesto com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatusCode.valueOf(400), "Manifesto com id listado não existe", 400);
                });
        logger.info("[ManifestoService.getPorId] Manifesto com ID {} encontrado", id);
        return manifesto;
    }

    public ManifestoResponse getManifestoResponsePorId(UUID id) {
        logger.info("[ManifestoService.getManifestoResponsePorId] Consultando manifesto com ID {}", id);
        ManifestoResponse response = new ManifestoResponse(getPorId(id));
        logger.info("[ManifestoService.getManifestoResponsePorId] Manifesto com ID {} consultado com sucesso", id);
        return response;
    }

    public void alterarStatus(UUID id, int status) {
        logger.info("[ManifestoService.alterarStatus] Alterando status do manifesto com ID {} para {}", id, status);
        var manifesto = getPorId(id);
        manifesto.setStatus(status);
        repository.save(manifesto);
        logger.info("[ManifestoService.alterarStatus] Status do manifesto com ID {} alterado com sucesso", id);
    }

    public void deletarPorId(UUID id) {
        logger.info("[ManifestoService.deletarPorId] Início da exclusão do manifesto com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[ManifestoService.deletarPorId] Manifesto com ID {} excluído com sucesso", id);
    }

    public Manifesto getManifestoPorData(LocalDate data, int tipo) {
        logger.info("[ManifestoService.getManifestoPorData] Consultando manifesto por data {} e tipo {}", data, tipo);
        Manifesto manifesto = repository.findByDataEmitidoAndTipo(data, tipo);
        if (manifesto != null) {
            logger.info("[ManifestoService.getManifestoPorData] Manifesto encontrado com data {} e tipo {}", data, tipo);
        } else {
            logger.info("[ManifestoService.getManifestoPorData] Nenhum manifesto encontrado com data {} e tipo {}", data, tipo);
        }
        return manifesto;
    }

    public Manifesto getManifestoVigente(TipoManifesto tipo) {
        logger.info("[ManifestoService.getManifestoVigente] Consultando manifesto vigente com tipo {}", tipo);
        var manifesto = getManifestoPorData(LocalDate.now(), tipo.getCodigo());
        if (manifesto == null) {
            logger.info("[ManifestoService.getManifestoVigente] Nenhum manifesto vigente encontrado. Criando novo manifesto com tipo {}", tipo);
            manifesto = repository.save(new Manifesto(tipo.getCodigo()));
            logger.info("[ManifestoService.getManifestoVigente] Novo manifesto criado com ID {}", manifesto.getIdManifesto());
        } else {
            logger.info("[ManifestoService.getManifestoVigente] Manifesto vigente encontrado com ID {}", manifesto.getIdManifesto());
        }
        return manifesto;
    }
}
