package agiliz.projetoAgiliz.dto.pagamento;

import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorGestaoResponse;
import agiliz.projetoAgiliz.dto.tipoColaborador.TipoColaboradorResponse;
import agiliz.projetoAgiliz.models.Pagamento;
import agiliz.projetoAgiliz.models.TipoColaborador;

public record PagamentoCadastroResponse(
        int tipoPagamento,
        Double remuneracao,
        TipoColaboradorGestaoResponse tipoColaborador
) {
    public PagamentoCadastroResponse(Pagamento pagamento){
        this(
                pagamento.getTipoPagamento().getCodigo(),
                pagamento.getRemuneracao(),
                new TipoColaboradorGestaoResponse(pagamento.getTipoColaborador())
        );
    }
}
