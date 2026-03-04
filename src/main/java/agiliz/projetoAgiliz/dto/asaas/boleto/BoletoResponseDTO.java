package agiliz.projetoAgiliz.dto.asaas.boleto;

import agiliz.projetoAgiliz.dto.asaas.pagamento.PagamentoResponseDTO;
import agiliz.projetoAgiliz.enums.AsaasEnum;
import agiliz.projetoAgiliz.models.Boleto;

import java.time.LocalDate;

public record BoletoResponseDTO(
        String idBoleto,
        String asaasId,
        String customer,
        LocalDate dateCreated,
        LocalDate dueDate,
        String billingType,
        Double value,
        String status,
        String paymentLink,
        String barcode,
        String bankSlipUrl,
        PagamentoResponseDTO pagamento
) {
    public BoletoResponseDTO(Boleto boleto) {
        this(
                boleto.getIdBoleto().toString(),
                boleto.getAsaasId(),
                boleto.getCustomer(),
                boleto.getDateCreated(),
                boleto.getDueDate(),
                boleto.getBillingType(),
                boleto.getValue(),
                AsaasEnum.fromCodigo(boleto.getStatus()).getTextoAmigavel(),
                boleto.getPaymentLink(),
                boleto.getBarcode(),
                boleto.getBankSlipUrl(),
                new PagamentoResponseDTO(boleto.getPagamentoAsaas())
        );
    }
}
