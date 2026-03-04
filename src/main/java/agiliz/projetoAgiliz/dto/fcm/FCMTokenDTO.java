package agiliz.projetoAgiliz.dto.fcm;

import jakarta.validation.constraints.NotBlank;

public record FCMTokenDTO(
        @NotBlank String fcm_token
) {
}
