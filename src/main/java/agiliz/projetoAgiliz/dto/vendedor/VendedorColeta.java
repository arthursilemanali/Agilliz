package agiliz.projetoAgiliz.dto.vendedor;

import agiliz.projetoAgiliz.models.Vendedor;

import java.util.UUID;

public record VendedorColeta(
    UUID idVendedor,
    String nome,
    String cnpj,
    String endereco
) {

    public VendedorColeta(Vendedor vendedor) {
        this(vendedor.getIdUnidade(), vendedor.getNomeVendedor(), vendedor.getCnpj(), vendedor.getRua() + " " + vendedor.getNumero());
    }
}
