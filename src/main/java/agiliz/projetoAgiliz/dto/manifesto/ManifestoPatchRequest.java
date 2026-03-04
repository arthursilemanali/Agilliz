package agiliz.projetoAgiliz.dto.manifesto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ManifestoPatchRequest(
   @NotNull
   @Positive
   Integer status
) {}
