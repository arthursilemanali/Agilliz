package agiliz.projetoAgiliz.dto.unidade;

import agiliz.projetoAgiliz.models.Vendedor;

import java.time.LocalTime;
import java.util.UUID;

public record VendedorResponse(
    UUID idVendedor,
    String nomeUnidade,
    String cnpj,
    String rua,
    String cep,
    Integer numero,
    String telefoneVendedor,
    Double retornoTotal,
    LocalTime horarioCorte
) {
    public VendedorResponse(Vendedor vendedor) {
        this(
            vendedor.getIdUnidade(),
            vendedor.getNomeVendedor(),
            vendedor.getCnpj(),
            vendedor.getRua(),
            vendedor.getCep(),
            vendedor.getNumero(),
            vendedor.getTelefoneVendedor(),
            vendedor.getRetornoTotal(),
            vendedor.getHorarioCorte()
        );
    }
}
