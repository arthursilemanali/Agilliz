package agiliz.projetoAgiliz.dto.coleta;

import agiliz.projetoAgiliz.models.Vendedor;

public record EnderecoColetaResponse(
        String rua,
        String cep,
        Integer numero
) {
    public EnderecoColetaResponse(Vendedor vendedor) {
        this(
                vendedor != null ? vendedor.getRua() : null,
                vendedor != null ? vendedor.getCep() : null,
                vendedor != null ? vendedor.getNumero() : null
        );
    }
}
