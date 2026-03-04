package agiliz.projetoAgiliz.dto.vendedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VendedorClientTg(
        @NotBlank String access_token,
        @NotBlank String refresh_token,
        @NotBlank String nickname,
        @NotNull Long id,
        @NotNull String email
        ) {
}
