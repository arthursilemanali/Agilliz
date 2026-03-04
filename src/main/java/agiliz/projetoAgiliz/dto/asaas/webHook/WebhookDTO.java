package agiliz.projetoAgiliz.dto.asaas.webHook;

public record WebhookDTO(
        String id,
        String event,
        PagamentoWebhookDTO payment
) {
}
