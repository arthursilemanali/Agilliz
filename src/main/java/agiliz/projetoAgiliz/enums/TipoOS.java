package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum TipoOS {
    UNICA(1, "Única"),
    GERAL(2, "Geral");

    private final int codigo;
    private final String alias;

    TipoOS(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    public static TipoOS valueOf(int codigo) {
        for (TipoOS tipo : TipoOS.values()) {
            if(tipo.codigo == codigo) return tipo;
        }

        throw new IllegalArgumentException("Código de tipo de OS inválido");
    }
}
