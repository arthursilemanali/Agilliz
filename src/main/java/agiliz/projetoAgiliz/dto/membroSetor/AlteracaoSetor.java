package agiliz.projetoAgiliz.dto.membroSetor;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AlteracaoSetor(@NotNull UUID fkSetor) {}
