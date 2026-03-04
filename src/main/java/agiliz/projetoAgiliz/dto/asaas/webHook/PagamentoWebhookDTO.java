package agiliz.projetoAgiliz.dto.asaas.webHook;

import java.time.LocalDate;

public record PagamentoWebhookDTO(
        String id,
        String status,
        Double value,
        LocalDate paidDate,
        String paymentMethod,
        String paymentLink,
        String barcode,
        String customer
) {
}
