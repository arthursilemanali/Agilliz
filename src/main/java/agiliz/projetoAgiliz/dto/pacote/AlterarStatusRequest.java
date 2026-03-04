package agiliz.projetoAgiliz.dto.pacote;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(
    @NotNull Integer status
) {}
