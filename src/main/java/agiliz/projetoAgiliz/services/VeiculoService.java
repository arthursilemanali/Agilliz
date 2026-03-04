package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.dto.veiculo.VeiculoRequest;
import agiliz.projetoAgiliz.dto.veiculo.VeiculoResponse;
import agiliz.projetoAgiliz.models.Veiculo;
import agiliz.projetoAgiliz.repositories.IVeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private static final Logger logger = LoggerFactory.getLogger(VeiculoService.class);
    private final IVeiculoRepository repository;
    private final ColaboradorService colaboradorService;

    public Veiculo getPorId(UUID id) {
        logger.info("[VeiculoService.getPorId] Consultando veículo com ID {}", id);
        if (!repository.existsById(id)) {
            logger.error("[VeiculoService.getPorId] Veículo com ID {} não encontrado", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado");
        }
        Veiculo veiculo = repository.findById(id).get();
        logger.info("[VeiculoService.getPorId] Veículo com ID {} encontrado", id);
        return veiculo;
    }

    public VeiculoResponse cadastrar(VeiculoRequest dto) {
        logger.info("[VeiculoService.cadastrar] Início da inserção do veículo");
        var veiculo = new Veiculo();
        veiculo.setColaborador(colaboradorService.getPorId(dto.fkColaborador()));
        BeanUtils.copyProperties(dto, veiculo);
        repository.save(veiculo);
        logger.info("[VeiculoService.cadastrar] Veículo inserido com sucesso: {}", veiculo);
        return new VeiculoResponse(veiculo);
    }

    public List<VeiculoResponse> getVeiculosResponse() {
        logger.info("[VeiculoService.getVeiculosResponse] Consultando veículos");
        var veiculos = repository.findAllResponse();
        logger.info("[VeiculoService.getVeiculosResponse] Veículos encontrados: {}", veiculos.size());
        return veiculos;
    }

    public VeiculoResponse getResponsePorId(UUID id) {
        return new VeiculoResponse(getPorId(id));
    }

    public void deletarPorId(UUID id) {
        logger.info("[VeiculoService.deletarPorId] Início da exclusão do veículo com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[VeiculoService.deletarPorId] Veículos com ID {} excluído com sucesso", id);
    }

    public List<VeiculoResponse> getVeiculosPorColaborador(UUID fkColaborador) {
        logger.info("[VeiculoService.getVeiculosPorColaborador] Consultando veículos do colaborador com ID {}", fkColaborador);
        var veiculos = repository.findVeiculosPorColaborador(colaboradorService.getPorId(fkColaborador));
        logger.info("[VeiculoService.getVeiculosResponse] Veículos encontrados: {}", veiculos.size());
        return veiculos;
    }
}
