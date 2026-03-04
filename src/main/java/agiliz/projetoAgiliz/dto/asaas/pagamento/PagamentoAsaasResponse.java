package agiliz.projetoAgiliz.dto.asaas.pagamento;

import agiliz.projetoAgiliz.enums.AsaasEnum;
import agiliz.projetoAgiliz.models.PagamentoAsaas;

import java.time.LocalDate;

public record PagamentoAsaasResponse(
        String idPagamento,
        String id,
        String status,
        Double value,
        LocalDate dataPagamento,
        String metodoPagamento,
        String linkPagamento,
        String codigoDeBarras,
        String cliente,
        boolean vencida,
        LocalDate dataVencimento
) {
    public PagamentoAsaasResponse(PagamentoAsaas pagamentoAsaas) {
        this(
                pagamentoAsaas.getIdPagamento().toString(),
                pagamentoAsaas.getId(),
                AsaasEnum.fromCodigo(pagamentoAsaas.getStatus()).getTextoAmigavel(),
                pagamentoAsaas.getValue(),
                pagamentoAsaas.getPaidDate(),
                pagamentoAsaas.getPaymentMethod(),
                pagamentoAsaas.getPaymentLink(),
                pagamentoAsaas.getBarcode(),
                pagamentoAsaas.getCustomer(),
                pagamentoAsaas.getStatus() == 3,
                pagamentoAsaas.getDataVencimento()
        );
    }
}
