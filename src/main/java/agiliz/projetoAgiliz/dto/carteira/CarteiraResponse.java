package agiliz.projetoAgiliz.dto.carteira;

import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.models.Carteira;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarteiraResponse(
    UUID idCarteira,
    int numeroConta,
    String chavePix,
    String agencia,
    Double saldo,
    ColaboradorResponse colaborador
) {

    public CarteiraResponse(Carteira carteira) {
        this(
            carteira.getIdCarteira(),
            carteira.getNumeroConta(),
            carteira.getChavePix(),
            carteira.getAgencia(),
            carteira.getSaldo(),
            carteira.getColaborador() != null
                ? new ColaboradorResponse(carteira.getColaborador())
                : null
        );
    }
}
