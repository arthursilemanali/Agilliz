package agiliz.projetoAgiliz.dto.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record VeiculoRequest(
    @NotNull @Positive Integer tipoVeiculo,
    @NotBlank @Pattern(
            regexp = "([A-Z]{2,3}[0-9]{4}|[A-Z]{3,4}[0-9]{3}|[A-Z0-9]{7})",
            message = "Placa inválida"
    ) String placa,
    @NotBlank String marca,
    @NotBlank String modelo,
    @NotNull UUID fkColaborador
) {}
