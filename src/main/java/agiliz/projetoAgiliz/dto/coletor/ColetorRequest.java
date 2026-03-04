package agiliz.projetoAgiliz.dto.coletor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ColetorRequest(
    @NotNull UUID colaborador,
    @NotNull UUID coleta,
    @NotNull @Positive Integer pacotesColetados
) {}
