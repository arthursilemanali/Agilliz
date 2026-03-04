package agiliz.projetoAgiliz.dto.pagamento;

import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagamentoCadastroRequest {
    @NotNull
    @Positive
    private Double remuneracao;
    @NotNull
    @Positive
    @Min(1)
    private int tipoPagamento;
    @NotNull
    private TipoColaboradorRequest tipoColaborador;
}
