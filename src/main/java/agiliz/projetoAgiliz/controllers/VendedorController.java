package agiliz.projetoAgiliz.controllers;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.unidade.VendedorRequest;
import agiliz.projetoAgiliz.dto.unidade.VendedorRequestPut;
import agiliz.projetoAgiliz.dto.vendedor.*;
import agiliz.projetoAgiliz.models.PrecificacaoZona;
import agiliz.projetoAgiliz.models.Vendedor;
import agiliz.projetoAgiliz.repositories.IVendedorRepository;
import agiliz.projetoAgiliz.repositories.VendedorRepositoryCustom;
import agiliz.projetoAgiliz.services.MensageriaService;
import agiliz.projetoAgiliz.services.PrecificacaoZonaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendedores")
@CrossOrigin
public class VendedorController {

    @Autowired
    private IVendedorRepository unidadeRepository;

    @Autowired
    private VendedorRepositoryCustom vendedorRepositoryCustom;

    @Autowired
    private PrecificacaoZonaService precificacaoZonaService;

    @GetMapping("/campos")
    public ResponseEntity<Map<String, String>> getCampos() {
        return ResponseEntity.ok(VendedorCampos.camposVendedor());
    }

    @GetMapping("/dashboard/filtros")
    public ResponseEntity<List<VendedorResponse>> findByFilters(@RequestParam String campo,
                                                                @RequestParam String valor) {
        if (Objects.equals(campo, "") || Objects.equals(valor, "")) {
            return ResponseEntity.ok(Objects.requireNonNull(listarUnidades()
                    .getBody()).getData());
        }

        Map<String, String> filtros = new HashMap<>();
        filtros.put(campo, valor);
        return ResponseEntity.ok(vendedorRepositoryCustom.buscarPorFiltros(filtros)
                .stream().map(VendedorResponse::new).toList());
    }

    @GetMapping("/horario-corte")
    public ResponseEntity<List<VendedorMLResponse>> getVendedoresByHorarioCorte(@RequestParam String horario) {
        try {
            var hora = LocalTime.parse(horario);
            return ResponseEntity.ok(unidadeRepository.getVendedoresByHorarioCorte(hora)
                    .stream().map(VendedorMLResponse::new).toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/ml/cadastro")
    public ResponseEntity<Void> cadastrarClienteTg(@RequestBody @Valid VendedorClientTg vendedorRequest) {
        try {
            var vendedor = new Vendedor();
            vendedor.setAcess_token(vendedorRequest.access_token());
            vendedor.setTg_token(vendedorRequest.refresh_token());
            vendedor.setNomeVendedor(vendedorRequest.nickname());
            vendedor.setId_ecommerce(vendedorRequest.id());
            vendedor.setEmail(vendedorRequest.email());

            if (unidadeRepository.countByEmailAndIdEcommerce(vendedorRequest.email(), vendedorRequest.id()) == 0)
                unidadeRepository.save(vendedor);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<MensageriaService<List<VendedorResponse>>> listarUnidades() {
        List<Vendedor> unidadeList = unidadeRepository.findAll();
        List<VendedorResponse> vendedorResponses = unidadeList.stream().map(VendedorResponse::new).toList();

        if (!vendedorResponses.isEmpty()) {
            MensageriaService<List<VendedorResponse>> mensageriaService =
                    new MensageriaService<>("Unidades encontradas", vendedorResponses, 200);
            return ResponseEntity.ok(mensageriaService);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tg/{idVendedor}")
    public ResponseEntity<MensageriaService<VendedorTgToken>> findVendedorTgById(@PathVariable String idVendedor) {
        var unidade = new VendedorTgToken(Objects.requireNonNull(listarUnidadePorId(UUID.fromString(idVendedor)).getBody()).getData());
        return ResponseEntity.status(200).body(
                new MensageriaService<VendedorTgToken>().data(unidade)
                        .mensagemCliente("Vendedor tg:")
        );
    }

    @GetMapping("/{tg_token}")
    public ResponseEntity<MensageriaService<VendedorTgToken>> findVendedorByAcessToken(@PathVariable String tg_token) {
        var vendedor = unidadeRepository.findVendedorByTgToken(tg_token).orElseThrow(() -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Vendedor com esse tg token não encontrado", 404));
        return ResponseEntity.status(200).body(new MensageriaService<VendedorTgToken>()
                .data(new VendedorTgToken(vendedor))
                .mensagemCliente("vendedor:").status(200));
    }

    @PutMapping("/atualizar-tg")
    public ResponseEntity<Void> atualizarTokens(@RequestBody List<VendedorTgToken> vendedores) {
        var clientesParaAtualizar = unidadeRepository
                .findAllById(vendedores.stream().map(VendedorTgToken::idVendedor).toList());
        var clientesMap = clientesParaAtualizar.stream()
                .collect(Collectors.toMap(Vendedor::getIdUnidade, Function.identity()));
        for (VendedorTgToken vendedor : vendedores) {
            var clienteAtual = clientesMap.get(vendedor.idVendedor());
            if (clienteAtual != null) {
                clienteAtual.setAcess_token(vendedor.acess_token());
            }
        }
        unidadeRepository.saveAll(clientesParaAtualizar);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all-tg")
    public ResponseEntity<List<VendedorTgToken>> listAllAcessToken() {
        List<VendedorTgToken> vendedores = unidadeRepository.findAll()
                .stream()
                .map(VendedorTgToken::new)
                .toList();
        return ResponseEntity.status(200).body(vendedores);
    }

    @GetMapping("/{idUnidade}")
    public ResponseEntity<MensageriaService<Vendedor>> listarUnidadePorId(@PathVariable UUID idUnidade) {
        Optional<Vendedor> unidade = unidadeRepository.findById(idUnidade);
        if (unidade.isPresent()) {
            MensageriaService mensageriaService = new MensageriaService("Vendedor", unidade, 200);
            return ResponseEntity.status(HttpStatus.OK).body(mensageriaService);
        }
        MensageriaService mensageriaService = new MensageriaService("Vendedor não encontrada", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensageriaService);
    }


    @PostMapping
    public ResponseEntity<MensageriaService<VendedorResponse>> cadastrarUnidade(
            @RequestBody @Valid VendedorRequest dto
    ) {
        Vendedor vendedor = new Vendedor();
        BeanUtils.copyProperties(dto, vendedor, "zonas");
        System.out.println(vendedor);
        vendedor.setRetornoTotal(0.);
        try {
            unidadeRepository.save(vendedor);
            precificacaoZonaService.cadastrar(dto.zonas(), vendedor);
            MensageriaService mensageriaService = new MensageriaService("Vendedor cadastrada com sucesso",
                    vendedor, 201);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensageriaService);
        } catch (Exception e) {
            MensageriaService mensageriaService = new MensageriaService(e, 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensageriaService);
        }
    }

    //Me desculpa 😥
    @PutMapping("/{idUnidade}")
    public ResponseEntity<MensageriaService<VendedorResponse>> alterarUnidadePorId(
            @PathVariable UUID idUnidade,
            @RequestBody @Valid VendedorRequestPut dto
    ) {
        Optional<Vendedor> optionalUnidade = unidadeRepository.findById(idUnidade);
        if (optionalUnidade.isPresent()) {
            Vendedor vendedorModel = optionalUnidade.get();
            BeanUtils.copyProperties(dto, vendedorModel, "zonas");
            vendedorModel.setIdUnidade(idUnidade);
            precificacaoZonaService.deleteAllByVendedorId(idUnidade);
            precificacaoZonaService.cadastrar(
                    (List<PrecificacaoZona>) (Object) dto.zonas().stream()
                            .map(zonaDTO -> new PrecificacaoZona(vendedorModel, zonaDTO))
                            .collect(Collectors.toList())
            );
            try {
                unidadeRepository.save(vendedorModel);

                MensageriaService<VendedorResponse> mensageriaService = new MensageriaService<>(
                        "Vendedor atualizado com sucesso",
                        new VendedorResponse(vendedorModel),
                        200
                );
                return ResponseEntity.ok(mensageriaService);
            } catch (Exception e) {
                System.err.println(e);
                MensageriaService<VendedorResponse> mensageriaService = new MensageriaService<>(
                        "Ocorreu um erro ao atualizar a unidade: " + e.getMessage(),
                        400
                );
                return ResponseEntity.badRequest().body(mensageriaService);
            }
        }
        MensageriaService<VendedorResponse> mensageriaService = new MensageriaService<>(
                "Vendedor não encontrado",
                404
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensageriaService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensageriaService<Vendedor>> deletarUnidade(@PathVariable UUID id) {
        Optional<Vendedor> unidade = unidadeRepository.findById(id);
        if (unidade.isPresent()) {
            try {
                unidadeRepository.deleteById(id);
                MensageriaService mensageriaService = new MensageriaService("Vendedor deletada com sucesso ",
                        200);
                return ResponseEntity.status(HttpStatus.OK).body(mensageriaService);
            } catch (Exception e) {
                MensageriaService mensageriaService = new MensageriaService("Ocorreu um erro ao deletar a unidade ",
                        unidade, 400);
                return ResponseEntity.status(HttpStatus.OK).body(mensageriaService);
            }
        }
        MensageriaService mensageriaService = new MensageriaService("Vendedor não encontrada ",
                unidade, 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensageriaService);
    }

}
