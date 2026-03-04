package agiliz.projetoAgiliz.dto.asaas.boleto;

import java.time.LocalDate;

public record BoletoRequestDTO(
        String id,
        String customer,
        LocalDate dateCreated,
        LocalDate dueDate,
        String billingType,
        Double value,
        String status,
        String paymentLink,
        String barcode,
        String bankSlipUrl,
        String idVendedor
) {
}
