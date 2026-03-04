package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorRequest;
import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorResponse;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.TipoColaboradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/tipos-colaborador")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TipoColaboradorController {

    @Autowired
    private TipoColaboradorService service;

    @PostMapping
    public ResponseEntity<MensageriaService<TipoColaboradorResponse>> cadastrar(
            @RequestBody @Valid TipoColaboradorRequest dto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(
          new MensageriaService<>(
                  "Tipo de colaborador cadastrado",
                  service.inserir(dto),
                  HttpStatus.CREATED.value()
          )
        );
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<TipoColaboradorResponse>>> listar(){
        var tiposColaborador = service.listar();

        if(tiposColaborador.isEmpty()) return noContent().build();

        return ok(
                new MensageriaService<List<TipoColaboradorResponse>>()
                        .mensagemCliente("Tipos colaboradores:")
                        .data(tiposColaborador)
                        .status(200)
        );
    }
}
