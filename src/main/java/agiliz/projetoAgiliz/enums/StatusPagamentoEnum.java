package agiliz.projetoAgiliz.enums;

import org.springframework.web.bind.annotation.GetMapping;

public enum StatusPagamentoEnum {
    PAGO(1, "pago"),
    AGUARDANDO_PAGAMENTO(2, "aguardando pagamento"),
    NAO_PAGO(3, "não pago");

    private final int codigo;
    private final String alias;

    StatusPagamentoEnum(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    public static StatusPagamentoEnum valueOf(int codigo) {
        for(StatusPagamentoEnum tipo : StatusPagamentoEnum.values()) {
            if(codigo == tipo.codigo) return tipo;
        }

        throw new IllegalArgumentException("Código inválido");
    }

    public String getAlias() {
        return alias;
    }
}
