package agiliz.projetoAgiliz.dto.coletor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ColetorPatchRequest(
   @NotNull @Positive Integer pacotesColetados
) {}
