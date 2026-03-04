package agiliz.projetoAgiliz.dto.pagamento;

import agiliz.projetoAgiliz.models.Pagamento;
import java.util.UUID;

public record PagamentoResponse(
        UUID idPagamento,
        String tipoPagamento,
        Double remuneracao,
        String taxado,
        String tipoColaborador
) {
    public PagamentoResponse(Pagamento pagamento) {
        this(
                pagamento.getIdPagamento(),
                pagamento.getTipoPagamento() != null ? pagamento.getTipoPagamento().getAlias() : null,
                pagamento.getRemuneracao(),
                pagamento.getTipoColaborador() != null
                        ? (pagamento.getTipoColaborador().isTaxado() ? "Sim" : "Não")
                        : "Não informado",
                pagamento.getTipoColaborador() != null
                        ? pagamento.getTipoColaborador().getDescricao()
                        : "Tipo não informado"
        );
    }
}
