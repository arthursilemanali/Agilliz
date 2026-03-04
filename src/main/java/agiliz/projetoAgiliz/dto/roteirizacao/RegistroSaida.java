package agiliz.projetoAgiliz.dto.roteirizacao;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record RegistroSaida(
    @NotNull Set<UUID> idsPacote,
    @NotNull Coordenada inicio,
    @NotNull Coordenada fim
) {}
