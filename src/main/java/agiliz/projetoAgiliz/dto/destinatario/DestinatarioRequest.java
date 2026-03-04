package agiliz.projetoAgiliz.dto.destinatario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DestinatarioRequest(
    @NotBlank @NotNull String nome,
    @NotBlank @NotNull String rua,
    @NotBlank @NotNull String cep,
    @NotNull Integer numero,
    @NotNull String celular,
    @NotNull String bairro,
    @NotNull String estado
) {}
