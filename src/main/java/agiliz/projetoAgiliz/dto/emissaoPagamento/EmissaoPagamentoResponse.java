package agiliz.projetoAgiliz.dto.emissaoPagamento;

import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.enums.StatusPagamentoEnum;
import agiliz.projetoAgiliz.models.EmissaoPagamento;

import java.time.LocalDate;
import java.util.UUID;

public record EmissaoPagamentoResponse(
        UUID idEmissaoPagamento,
        ColaboradorResponse colaborador,
        LocalDate data,
        Double valor,
        String pago,
        String cpfColaborador
) {
    public EmissaoPagamentoResponse(EmissaoPagamento emissao) {
        this(
                emissao.getIdEmissaoPagamento(),
                emissao.getColaborador() != null ? new ColaboradorResponse(emissao.getColaborador()) : null,
                emissao.getData(),
                emissao.getValor(),
                StatusPagamentoEnum.valueOf(emissao.getStatusPagamento()).getAlias(),
                emissao.getColaborador() != null ? emissao.getColaborador().getCpf() : null
        );
    }
}
