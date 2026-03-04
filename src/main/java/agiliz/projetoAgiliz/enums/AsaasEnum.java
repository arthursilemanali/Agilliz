package agiliz.projetoAgiliz.enums;

public enum AsaasEnum {
    PAYMENT_UPDATED(1, "Alteração no vencimento ou valor da cobrança"),
    PAYMENT_RECEIVED(2, "Cobrança recebida"),
    PAYMENT_OVERDUE(3, "Cobrança vencida"),
    PAYMENT_CONFIRMED(4, "Cobrança confirmada"),
    PAYMENT_BANK_SLIP_VIEWED(5, "Boleto visualizado pelo cliente"),
    PENDING(6, "Aguardando pagamento"),
    PAYMENT_CHECKOUT_VIEWED(7, "Fatura da cobrança visualizada pelo cliente.");

    private final int codigo;
    private final String textoAmigavel;

    AsaasEnum(int codigo, String textoAmigavel) {
        this.codigo = codigo;
        this.textoAmigavel = textoAmigavel;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTextoAmigavel() {
        return textoAmigavel;
    }

    public static AsaasEnum fromString(String status) {
        for (AsaasEnum ps : AsaasEnum.values()) {
            if (ps.name().equals(status)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + status);
    }

    public static AsaasEnum fromCodigo(int codigo) {
        for (AsaasEnum ps : AsaasEnum.values()) {
            if (ps.getCodigo() == codigo) {
                return ps;
            }
        }
        throw new IllegalArgumentException("Código desconhecido: " + codigo);
    }
}
