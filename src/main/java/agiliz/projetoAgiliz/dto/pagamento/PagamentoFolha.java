package agiliz.projetoAgiliz.dto.pagamento;

import agiliz.projetoAgiliz.models.EmissaoPagamento;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagamentoFolha(
    UUID idEmissaoPagamento,
    LocalDate data,
    Double valor
) {
    public PagamentoFolha(EmissaoPagamento emissao) {
        this(
            emissao.getIdEmissaoPagamento(),
            emissao.getData(),
            emissao.getValor()
        );
    }
}
