package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.colaborador.*;
import agiliz.projetoAgiliz.dto.fcm.FCMTokenDTO;
import agiliz.projetoAgiliz.services.ColaboradorService;
import agiliz.projetoAgiliz.services.MensageriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/funcionario")
@CrossOrigin
public class ColaboradorController {
    @Autowired
    private ColaboradorService service;

    @GetMapping("/campos")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> getCamposColaborador() {
        return new ResponseEntity<>(ColaboradorCampos.camposColaborador(), HttpStatus.OK);
    }

    @PatchMapping("/fcm/{idColaborador}")
    @PreAuthorize("hasRole('ROLE_MOTOBOY')")
    public ResponseEntity<Void> alterarFcm(@PathVariable String idColaborador,
                                           @RequestBody @Valid FCMTokenDTO token) {
        try {
            service.atualizarToken(token.fcm_token(), idColaborador);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    400);
        }
    }

    @PostMapping("/email-recuperacao-senha")
    public ResponseEntity<Void> solicitarAlteracaoSenha(
            @RequestBody @Valid EmailAlterarSenhaRequest dto
    ) {
        service.mandarEmailAlteracaoSenha(dto.email());
        return noContent().build();
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid AlterarSenhaRequest dto) {
        service.alterarSenha(dto);
        return noContent().build();
    }

    @PostMapping("/permissoes")
    public ResponseEntity<List<String>> getPermissoes(@RequestBody @Valid TokenRequestDTO token) {
        List<String> roles = service.extractRoles(token.token());
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/login/nonAdmin")
    ResponseEntity<UsuarioLoginDTO> loginNonAdmin(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        var userLogin = service.login(usuarioLoginDTO);
        if (userLogin != null) {
            return status(HttpStatus.OK).body(userLogin);
        }
        return status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/login")
    ResponseEntity<UsuarioLoginDTO> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        var userLogin = service.login(usuarioLoginDTO);
        if (userLogin != null) {
            return status(HttpStatus.OK).body(userLogin);
        }
        return status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/cadastrar")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "colaboradoresCache", allEntries = true)
    public ResponseEntity<MensageriaService<ColaboradorResponse>> cadastroColaborador(
            @RequestBody @Valid ColaboradorRequest dto
    ) {
        return status(HttpStatus.CREATED).body(
                new MensageriaService<ColaboradorResponse>()
                        .mensagemCliente("Colaborador cadastrado com sucesso")
                        .data(service.inserir(dto))
                        .status(201)
        );
    }

    @PutMapping("/alterar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "colaboradoresCache", allEntries = true)
    public ResponseEntity<MensageriaService<ColaboradorResponse>> alterarColaboradorById(
            @PathVariable UUID id,
            @Valid @RequestBody ColaboradorRequestPut dto
    ) {
        return ok(
                new MensageriaService<ColaboradorResponse>()
                        .mensagemCliente("Colaborador alterado com sucesso")
                        .data(service.alterar(id, dto))
                        .status(200)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CacheEvict(value = "colaboradoresCache", allEntries = true)
    public ResponseEntity<MensageriaService<Void>> deletarPorId(@PathVariable UUID id) {
        service.deletarPorId(id);
        return noContent().build();
    }

    @GetMapping("/filtro")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MensageriaService<List<ColaboradorResponse>>> findByFiltros(@RequestParam String campo, @RequestParam String valor) {
        return ok(
                new MensageriaService<List<ColaboradorResponse>>()
                        .mensagemCliente("Colaboradores filtrados")
                        .status(200)
                        .data(service.findByFiltros(campo, valor))
        );
    }

    @GetMapping("/pacotes-entrega")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MOTOBOY')")
    public ResponseEntity<MensageriaService<List<ColaboradorPacoteResponse>>> getPacotesColaboradorPorcentagem() {
        return ok(
                new MensageriaService<List<ColaboradorPacoteResponse>>()
                        .mensagemCliente("Pacotes entregues e para entregar de hoje")
                        .status(200)
                        .data(service.getColaboradoresPacote())
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Cacheable("colaboradoresCache")
    public ResponseEntity<MensageriaService<Page<ColaboradorResponse>>> getColaboradores(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho
    ) {
        var colaboradores = service.getColaboradoresResponsePaginados(pagina, tamanho);
        if (colaboradores.isEmpty()) return noContent().build();
        return ok(
                new MensageriaService<Page<ColaboradorResponse>>()
                        .mensagemCliente("Funcionários")
                        .data(colaboradores)
                        .status(200)
        );
    }

    @GetMapping("/nao-paginados")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Cacheable("colaboradoresCache")
    public ResponseEntity<MensageriaService<List<ColaboradorResponse>>> getColaboradores() {
        var colaboradores = service.getColaboradoresResponse();
        if (colaboradores.isEmpty()) return noContent().build();
        return ok(
                new MensageriaService<List<ColaboradorResponse>>()
                        .mensagemCliente("Funcionários")
                        .data(colaboradores)
                        .status(200)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MensageriaService<ColaboradorResponse>> listarFuncionariosPorId(
            @PathVariable UUID id
    ) {
        return status(HttpStatus.OK)
                .body(
                        new MensageriaService<ColaboradorResponse>()
                                .mensagemCliente("Funcionário")
                                .data(service.getResponsePorId(id))
                                .status(200)
                );
    }

    @GetMapping("/folha-pagamento")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_FINANCEIRO')")
    public ResponseEntity<MensageriaService<FolhaDePagamento>> getFolhaPagamento() {
        return ok(
                new MensageriaService<FolhaDePagamento>()
                        .mensagemCliente("Folha pagamento")
                        .data(service.gerarFolhaDePagamento())
                        .status(200)
        );
    }
}
