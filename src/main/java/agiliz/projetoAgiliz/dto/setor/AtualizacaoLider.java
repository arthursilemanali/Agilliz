package agiliz.projetoAgiliz.dto.setor;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AtualizacaoLider(@NotNull UUID fkLider) {}
