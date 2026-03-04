package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum OrigemPacote {
    MERCADO_LIVRE(1, "Mercado livre"),
    SHOPEE(2, "Shopee"),
    OUTROS(3, "Outros");

    OrigemPacote(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    private final int codigo;
    private final String alias;

    public static OrigemPacote valueOf(int codigo) {
        for(OrigemPacote origem : OrigemPacote.values()) {
            if(origem.getCodigo() == codigo) return origem;
        }

        throw new IllegalArgumentException("Não existe origem com esse código");
    }
}
