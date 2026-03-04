package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.enderecoFinal.EnderecoFinalRequest;
import agiliz.projetoAgiliz.dto.enderecoFinal.EnderecoFinalResponse;
import agiliz.projetoAgiliz.models.EnderecoFinal;
import agiliz.projetoAgiliz.repositories.IEnderecoFinalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnderecoFinalService {

    private final IEnderecoFinalRepository repository;
    private final ColaboradorService colaboradorService;

    private static final Logger logger = LoggerFactory.getLogger(EnderecoFinalService.class);

    public EnderecoFinalResponse cadastrar(EnderecoFinalRequest enderecoFinalDTO) {
        logger.info("[EnderecoFinalService.cadastrar] Início do cadastro de Endereço Final para colaborador com ID {}", enderecoFinalDTO.fkFuncionario());

        var enderecoFinal = new EnderecoFinal();
        enderecoFinal.setColaborador(colaboradorService.getPorId(enderecoFinalDTO.fkFuncionario()));
        BeanUtils.copyProperties(enderecoFinalDTO, enderecoFinal);

        EnderecoFinalResponse response = new EnderecoFinalResponse(repository.save(enderecoFinal));
        logger.info("[EnderecoFinalService.cadastrar] Endereço Final cadastrado com sucesso para colaborador com ID {}", enderecoFinalDTO.fkFuncionario());

        return response;
    }

    public EnderecoFinalResponse getEnderecoFinalResponsePorId(UUID id) {
        logger.info("[EnderecoFinalService.getEnderecoFinalResponsePorId] Consultando Endereço Final com ID {}", id);
        EnderecoFinalResponse response = new EnderecoFinalResponse(getPorId(id));
        logger.info("[EnderecoFinalService.getEnderecoFinalResponsePorId] Endereço Final com ID {} consultado com sucesso", id);
        return response;
    }

    public EnderecoFinal getPorId(UUID idEnderecoFinal) {
        logger.info("[EnderecoFinalService.getPorId] Consultando Endereço Final com ID {}", idEnderecoFinal);
        EnderecoFinal enderecoFinal = repository.findById(idEnderecoFinal)
                .orElseThrow(() -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Endereço Final não encontrado", 404));
        logger.info("[EnderecoFinalService.getPorId] Endereço Final com ID {} encontrado", idEnderecoFinal);
        return enderecoFinal;
    }

    public EnderecoFinalResponse alterar(UUID id, EnderecoFinalRequest dto) {
        logger.info("[EnderecoFinalService.alterar] Início da alteração do Endereço Final com ID {}", id);

        EnderecoFinal enderecoFinal = getPorId(id);
        BeanUtils.copyProperties(dto, enderecoFinal);

        EnderecoFinalResponse response = new EnderecoFinalResponse(repository.save(enderecoFinal));
        logger.info("[EnderecoFinalService.alterar] Endereço Final com ID {} alterado com sucesso", id);

        return response;
    }

    public void deletarEnderecoFinalPorId(UUID id) {
        logger.info("[EnderecoFinalService.deletarEnderecoFinalPorId] Início da exclusão do Endereço Final com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[EnderecoFinalService.deletarEnderecoFinalPorId] Endereço Final com ID {} excluído com sucesso", id);
    }

    public List<EnderecoFinalResponse> getEnderecosColaborador(UUID fkColaborador) {
        logger.info("[EnderecoFinalService.getEnderecosColaborador] Consultando endereços finais para colaborador com ID {}", fkColaborador);
        List<EnderecoFinalResponse> response = repository.findResponseByColaborador(colaboradorService.getPorId(fkColaborador));
        logger.info("[EnderecoFinalService.getEnderecosColaborador] Endereços finais consultados com sucesso para colaborador com ID {}", fkColaborador);
        return response;
    }
}
