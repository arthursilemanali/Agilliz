package agiliz.projetoAgiliz.dto.vendedor;

import agiliz.projetoAgiliz.models.Vendedor;

import java.time.LocalTime;

public record VendedorMLResponse(
            Long id_ecommerce,
            LocalTime horarioCorte,
            String acess_token,
            String idVendedor
) {
    public VendedorMLResponse(Vendedor vendedor) {
        this(
                vendedor.getId_ecommerce(),
                vendedor.getHorarioCorte(),
                vendedor.getAcess_token(),
                vendedor.getIdUnidade().toString()
        );
    }
}
