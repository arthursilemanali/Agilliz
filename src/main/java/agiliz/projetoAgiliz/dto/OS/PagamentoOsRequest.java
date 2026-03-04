package agiliz.projetoAgiliz.dto.OS;

import agiliz.projetoAgiliz.dto.pacote.PacoteOS;
import agiliz.projetoAgiliz.dto.pacote.PacoteRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;
import java.util.UUID;

public record PagamentoOsRequest(
        @NotNull String idColaborador,
        @NotNull @PositiveOrZero Double valor
)
{}
