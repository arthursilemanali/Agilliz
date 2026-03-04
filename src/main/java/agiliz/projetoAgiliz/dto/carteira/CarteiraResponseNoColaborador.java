package agiliz.projetoAgiliz.dto.carteira;

import agiliz.projetoAgiliz.models.Carteira;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarteiraResponseNoColaborador(
        String chavePix
) {

    public CarteiraResponseNoColaborador(Carteira carteira) {
        this(
                carteira.getChavePix()
        );
    }
}
