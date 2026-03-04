package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.comprovanteEntrega.ComprovanteEntregaRequest;
import agiliz.projetoAgiliz.dto.comprovanteEntrega.ComprovanteEntregaResponse;
import agiliz.projetoAgiliz.models.ComprovanteEntrega;
import agiliz.projetoAgiliz.repositories.IComprovanteEntregaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComprovanteEntregaService {

    private final IComprovanteEntregaRepository repository;
    private final PacoteService pacoteService;
    private static final Logger logger = LoggerFactory.getLogger(ComprovanteEntregaService.class);

    public ComprovanteEntregaResponse cadastrar(ComprovanteEntregaRequest dto) {
        logger.info("[ComprovanteEntregaService.cadastrar] Início do cadastro do comprovante de entrega para o pacotes com ID {}", dto.fkPacote());

        var comprovante = new ComprovanteEntrega();
        comprovante.setPacote(pacoteService.getPorId(dto.fkPacote()));
        BeanUtils.copyProperties(dto, comprovante);

        ComprovanteEntregaResponse response = new ComprovanteEntregaResponse(repository.save(comprovante));
        logger.info("[ComprovanteEntregaService.cadastrar] Comprovante de entrega cadastrado com sucesso para o pacotes com ID {}", dto.fkPacote());

        return response;
    }

    public Page<ComprovanteEntregaResponse> getComprovantesResponsePaginados(int pagina, int tamanho) {
        logger.info("[ComprovanteEntregaService.getComprovantesResponsePaginados] Consultando comprovantes de entrega paginados - Página: {}, Tamanho: {}", pagina, tamanho);

        var page = PageRequest.of(pagina, tamanho, Sort.by("dataHoraEmissao").descending());
        Page<ComprovanteEntregaResponse> response = repository.findAllResponsePaginados(page);

        logger.info("[ComprovanteEntregaService.getComprovantesResponsePaginados] Comprovantes de entrega paginados consultados com sucesso - Página: {}, Tamanho: {}", pagina, tamanho);
        return response;
    }

    public ComprovanteEntregaResponse getResponsePorId(UUID id) {
        logger.info("[ComprovanteEntregaService.getResponsePorId] Consultando comprovante de entrega com ID {}", id);
        ComprovanteEntregaResponse response = new ComprovanteEntregaResponse(getPorId(id));
        logger.info("[ComprovanteEntregaService.getResponsePorId] Comprovante de entrega com ID {} encontrado", id);
        return response;
    }

    public ComprovanteEntrega getPorId(UUID id) {
        logger.info("[ComprovanteEntregaService.getPorId] Consultando comprovante de entrega com ID {}", id);
        ComprovanteEntrega comprovante = repository.findById(id)
                .orElseThrow(() -> new ResponseEntityException(HttpStatusCode.valueOf(404), "Comprovante com id listado não existe", 404));
        logger.info("[ComprovanteEntregaService.getPorId] Comprovante de entrega com ID {} encontrado", id);
        return comprovante;
    }

    public ComprovanteEntregaResponse alterar(ComprovanteEntregaRequest dto, UUID id) {
        logger.info("[ComprovanteEntregaService.alterar] Início da alteração do comprovante de entrega com ID {}", id);

        var comprovante = getPorId(id);
        comprovante.setPacote(pacoteService.getPorId(dto.fkPacote()));
        BeanUtils.copyProperties(dto, comprovante);

        ComprovanteEntregaResponse response = new ComprovanteEntregaResponse(repository.save(comprovante));
        logger.info("[ComprovanteEntregaService.alterar] Comprovante de entrega com ID {} alterado com sucesso", id);

        return response;
    }

    public void deletarPorId(UUID id) {
        logger.info("[ComprovanteEntregaService.deletarPorId] Início da exclusão do comprovante de entrega com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[ComprovanteEntregaService.deletarPorId] Comprovante de entrega com ID {} excluído com sucesso", id);
    }
}