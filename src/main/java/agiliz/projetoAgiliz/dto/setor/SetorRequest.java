package agiliz.projetoAgiliz.dto.setor;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SetorRequest(
        @NotBlank String limiteInferiorCep,
        @NotBlank String limiteSuperiorCep,
        UUID fkLiderSetor,
        Double valorBonificacao
) {}
