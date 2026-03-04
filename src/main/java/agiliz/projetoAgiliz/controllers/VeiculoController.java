package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.veiculo.VeiculoRequest;
import agiliz.projetoAgiliz.dto.veiculo.VeiculoResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/veiculos")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class VeiculoController {
    @Autowired
    private VeiculoService service;

    @PostMapping
    public ResponseEntity<MensageriaService<VeiculoResponse>> cadastrar(
            @RequestBody @Valid VeiculoRequest dto
    ) {
        return ok(
                new MensageriaService<VeiculoResponse>()
                        .data(service.cadastrar(dto))
                        .mensagemCliente("Veículo cadastrado com sucesso")
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<VeiculoResponse>>> listarVeiculos() {
        var veiculos = service.getVeiculosResponse();

        if (veiculos.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<List<VeiculoResponse>>()
                        .data(veiculos)
                        .mensagemCliente("Veículos:")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<VeiculoResponse>> listarVeiculosPorId(@PathVariable UUID id) {
        return ok(
                new MensageriaService<VeiculoResponse>()
                        .data(service.getResponsePorId(id))
                        .mensagemCliente("Veículos:")
        );
    }

    @GetMapping("/veiculos-colaborador/{fkColaborador}")
    public ResponseEntity<MensageriaService<List<VeiculoResponse>>> listarVeiculosPorColaborador(
            @PathVariable UUID fkColaborador
    ) {
        return ok(
                new MensageriaService<List<VeiculoResponse>>()
                        .data(service.getVeiculosPorColaborador(fkColaborador))
                        .mensagemCliente("Veículos:")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculosPorId(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }
}
