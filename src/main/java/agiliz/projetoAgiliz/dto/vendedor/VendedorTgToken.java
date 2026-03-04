package agiliz.projetoAgiliz.dto.vendedor;

import agiliz.projetoAgiliz.models.Vendedor;

import java.util.UUID;

public record VendedorTgToken(
        UUID idVendedor,
        String code,
        String acess_token
) {
    public VendedorTgToken(Vendedor vendedor) {
        this(
                vendedor.getIdUnidade(),
                vendedor.getTg_token(),
                vendedor.getAcess_token()
        );
    }
}
