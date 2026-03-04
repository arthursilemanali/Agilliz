package agiliz.projetoAgiliz.dto.despesa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// adicionar validação pra se for mensal especificar o dia
public record DespesaRequest(
    @NotNull Integer tipoDespesa,
    @NotNull Double valorDespesa,
    @NotNull Integer vigencia,
    @NotBlank String descricaoDespesa,
    String fkVeiculo,
    Integer diaVigencia
) {}
