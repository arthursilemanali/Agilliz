package agiliz.projetoAgiliz.dto.asaas.pagamento;

import agiliz.projetoAgiliz.enums.AsaasEnum;
import agiliz.projetoAgiliz.models.PagamentoAsaas;

import java.time.LocalDate;

public record PagamentoResponseDTO(
        String status,
        LocalDate paidDate
) {
    public PagamentoResponseDTO(PagamentoAsaas pagamentoAsaas){
        this(
                AsaasEnum.fromCodigo(pagamentoAsaas.getStatus()).getTextoAmigavel(),
                pagamentoAsaas.getPaidDate()
        );
    }
}
