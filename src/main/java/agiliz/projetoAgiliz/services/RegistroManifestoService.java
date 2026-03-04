package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoRequest;
import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoResponse;
import agiliz.projetoAgiliz.enums.TipoManifesto;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.RegistroManifesto;
import agiliz.projetoAgiliz.repositories.IRegistroManifestoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistroManifestoService {
    private final IRegistroManifestoRepository repository;
    private final ManifestoService manifestoService;
    private final PacoteService pacoteService;

    private static final Logger logger = LoggerFactory.getLogger(RegistroManifestoService.class);

    public void cadastrar(List<RegistroManifesto> registros){
        logger.info("[RegistroManifestoService.cadastrar] Início do cadastro de registro de manifesto com dados: {}", registros);
        repository.saveAll(registros);
        logger.info("[RegistroManifestoService.cadastrar] Registros cadastrados com sucesso");
    }

    public RegistroManifestoResponse cadastrar(RegistroManifestoRequest dto) {
        logger.info("[RegistroManifestoService.cadastrar] Início do cadastro de registro de manifesto com dados: {}", dto);
        var registro = new RegistroManifesto(
                manifestoService.getPorId(dto.fkManifesto()),
                pacoteService.getPorId(dto.fkPacote())
        );

        RegistroManifestoResponse response = new RegistroManifestoResponse(repository.save(registro));
        logger.info("[RegistroManifestoService.cadastrar] Registro de manifesto cadastrado com sucesso com ID {}", registro.getIdRegistroManifesto());
        return response;
    }

    public Page<RegistroManifestoResponse> getRegistrosManifestosResponsePaginados(int pagina, int tamanho) {
        logger.info("[RegistroManifestoService.getRegistrosManifestosResponsePaginados] Consultando registros de manifesto paginados: página {}, tamanho {}", pagina, tamanho);
        var page = PageRequest.of(pagina, tamanho, Sort.by("dataResgistro").descending());
        Page<RegistroManifestoResponse> registrosPaginados = repository.findAllResponsePaginados(page);
        logger.info("[RegistroManifestoService.getRegistrosManifestosResponsePaginados] Total de registros de manifesto encontrados: {}", registrosPaginados.getTotalElements());
        return registrosPaginados;
    }

    public RegistroManifestoResponse getRegistroManifestoResponsePorId(UUID id) {
        logger.info("[RegistroManifestoService.getRegistroManifestoResponsePorId] Consultando registro de manifesto com ID {}", id);
        RegistroManifestoResponse response = new RegistroManifestoResponse(getPorId(id));
        logger.info("[RegistroManifestoService.getRegistroManifestoResponsePorId] Registro de manifesto com ID {} encontrado", id);
        return response;
    }

    public RegistroManifesto getPorId(UUID id) {
        logger.info("[RegistroManifestoService.getPorId] Consultando registro de manifesto com ID {}", id);
        RegistroManifesto registro = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[RegistroManifestoService.getPorId] Registro de manifesto com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Registro de manifesto com o código listado não existe", 404);
                });
        logger.info("[RegistroManifestoService.getPorId] Registro de manifesto com ID {} encontrado", id);
        return registro;
    }

    public void alterarManifesto(UUID id, UUID fkManifesto) {
        logger.info("[RegistroManifestoService.alterarManifesto] Alterando manifesto do registro com ID {} para o manifesto com ID {}", id, fkManifesto);
        var registro = getPorId(id);
        registro.setManifesto(manifestoService.getPorId(fkManifesto));
        repository.save(registro);
        logger.info("[RegistroManifestoService.alterarManifesto] Manifesto do registro com ID {} alterado com sucesso", id);
    }

    public void deletarPorId(UUID id) {
        logger.info("[RegistroManifestoService.deletarPorId] Início da exclusão do registro de manifesto com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[RegistroManifestoService.deletarPorId] Registro de manifesto com ID {} excluído com sucesso", id);
    }

    private void criarRegistrosSaida(Collection<Pacote> pacotes) {
        var manifesto = manifestoService.getManifestoVigente(TipoManifesto.SAIDA);

        var registros = pacotes.stream()
            .map(p -> new RegistroManifesto(manifesto, p))
            .toList();

        repository.saveAll(registros);
        logger.info("[RegistroManifestoService.registrarSaida] Saída registrada com sucesso para {} pacotes", pacotes.size());
    }
}
