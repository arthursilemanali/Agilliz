package agiliz.projetoAgiliz.dto.despesa;

import agiliz.projetoAgiliz.dto.veiculo.VeiculoResponse;
import agiliz.projetoAgiliz.models.Despesa;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DespesaResponse(
    UUID idDespesa,
    String tipoDespesa,
    String vigencia,
    Double valorDespesa,
    VeiculoResponse veiculo,
    String descricaoDespesa,
    LocalDate dataCriacao,
    Integer diaVigencia
) {
    public DespesaResponse(Despesa despesa) {
        this(
            despesa.getIdDespesa(),
            despesa.getTipoDespesa().getAlias(),
            despesa.getVigencia().getAlias(),
            despesa.getValorDespesa(),
            despesa.getVeiculo() != null
                    ? new VeiculoResponse(despesa.getVeiculo())
                    : null,
            despesa.getDescricaoDespesa(),
            despesa.getDataCadastrado(),
            despesa.getDiaVigencia()
        );
    }
}
