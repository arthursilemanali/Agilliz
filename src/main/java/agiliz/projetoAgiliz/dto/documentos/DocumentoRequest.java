package agiliz.projetoAgiliz.dto.documentos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record DocumentoRequest(
        @NotBlank
        String idColaboradorCTPS,
        @NotBlank
        String idColaboradorHabilitacao,
        @NotBlank
        String idColaboradorReservista,
        @NotBlank
        String idColaboradorResicencia
) {
}
