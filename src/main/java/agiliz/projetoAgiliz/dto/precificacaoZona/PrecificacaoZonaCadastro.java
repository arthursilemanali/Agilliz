package agiliz.projetoAgiliz.dto.precificacaoZona;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PrecificacaoZonaCadastro(
        @NotNull @Positive Double preco,
        @NotNull int tipoZona
) {
}
