package agiliz.projetoAgiliz.dto.vendedor;

import agiliz.projetoAgiliz.dto.asaas.pagamento.PagamentoAsaasResponse;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoResponseNoVendedor;
import agiliz.projetoAgiliz.models.Vendedor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record VendedorResponse(
        UUID idVendedor,
        List<PrecificacaoResponseNoVendedor> zonas,
        String nomeVendedor,
        String cnpj,
        String rua,
        String cep,
        Integer numero,
        String telefoneVendedor,
        double retornoTotal,
        LocalTime horarioCorte,
        String email,
        List<String> ecommerces,
        String nomeFantasia,
        List<PagamentoAsaasResponse> pagamentos,
        String bairro,
        String cidade,
        String estado
) {
    public VendedorResponse(Vendedor vendedor) {
        this(
                vendedor.getIdUnidade(),
                vendedor.getZonas().stream()
                        .map(PrecificacaoResponseNoVendedor::new)
                        .collect(Collectors.toList()),
                vendedor.getNomeVendedor(),
                vendedor.getCnpj(),
                vendedor.getRua(),
                vendedor.getCep(),
                vendedor.getNumero(),
                vendedor.getTelefoneVendedor(),
                vendedor.getRetornoTotal(),
                vendedor.getHorarioCorte(),
                vendedor.getEmail(),
                Ecommerces.getEcommerces(vendedor.getPacotes()),
                vendedor.getNomeFantasia(),
                vendedor.getPagamentos().stream().map(PagamentoAsaasResponse::new).toList(),
                vendedor.getBairro(),
                vendedor.getCidade(),
                vendedor.getEstado()
        );
    }
}
