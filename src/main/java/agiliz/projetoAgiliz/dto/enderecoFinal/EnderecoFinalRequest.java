package agiliz.projetoAgiliz.dto.enderecoFinal;

import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnderecoFinalRequest(
    @NotNull UUID fkFuncionario,
    @NotBlank String apelido,
    @NotBlank String cep,
    @NotBlank String rua,
    String numero,
    String complemento,
    @NotNull Coordenada localizacao,
    String bairro,
    String estado
) {}
