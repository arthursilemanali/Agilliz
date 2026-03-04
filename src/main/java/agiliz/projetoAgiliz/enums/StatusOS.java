package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum StatusOS {
    PAGO(1, "Pago"),
    PENDENTE(2, "Pendente");

    private final int codigo;
    private final String alias;

    StatusOS(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    public static StatusOS valueOf(int codigo) {
        for(StatusOS status : StatusOS.values()) {
            if(status.codigo == codigo) return status;
        }

        throw new IllegalArgumentException("Código de status OS inválido");
    }
}
