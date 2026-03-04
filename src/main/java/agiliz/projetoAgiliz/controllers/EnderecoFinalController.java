package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.enderecoFinal.EnderecoFinalRequest;
import agiliz.projetoAgiliz.dto.enderecoFinal.EnderecoFinalResponse;
import agiliz.projetoAgiliz.services.EnderecoFinalService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/enderecos")
@CrossOrigin
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MOTOBOY')")
public class EnderecoFinalController {

    @Autowired
    private EnderecoFinalService service;

    @PostMapping
    public ResponseEntity<MensageriaService<EnderecoFinalResponse>> cadastrar(
        @RequestBody @Valid EnderecoFinalRequest dto
    ){
        return status(HttpStatus.OK).body(new MensageriaService<EnderecoFinalResponse>()
                .mensagemCliente("Endereço Cadastrado com Sucesso")
                .data(service.cadastrar(dto))
                .status(200)
        );
    }

    @GetMapping("/enderecos-colaborador/{fkColaborador}")
    public ResponseEntity<MensageriaService<List<EnderecoFinalResponse>>> getEnderecosColaborador(
        @PathVariable UUID fkColaborador
    ) {
        var enderecos = service.getEnderecosColaborador(fkColaborador);

        if(enderecos.isEmpty()) return noContent().build();

        return ok(
            new MensageriaService<List<EnderecoFinalResponse>>()
                .mensagemCliente("Endereços de colaborador:")
                .data(enderecos)
                .status(200)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensageriaService<EnderecoFinalResponse>> getEnderecoPorId(
        @PathVariable UUID id
    ) {
        return status(HttpStatus.OK).body(new MensageriaService<EnderecoFinalResponse>()
                .mensagemCliente("Coordenadas Final:")
                .data(service.getEnderecoFinalResponsePorId(id))
                .status(200)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensageriaService<EnderecoFinalResponse>> alterarEnderecoFinalPorId(
        @PathVariable UUID id,
        @RequestBody @Valid EnderecoFinalRequest dto
    ) {
        return ok(
            new MensageriaService<EnderecoFinalResponse>()
                .mensagemCliente("Coordenadas Final alterado com sucesso")
                .data(service.alterar(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEnderecoFinalPorId(@PathVariable UUID id) {
        service.deletarEnderecoFinalPorId(id);
        return noContent().build();
    }
}
