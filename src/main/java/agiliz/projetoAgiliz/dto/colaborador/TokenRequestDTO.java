package agiliz.projetoAgiliz.dto.colaborador;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDTO(
        @NotBlank String token
) {
}
