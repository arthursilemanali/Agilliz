package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum TipoPagamento {
    MERCADO_LIVRE(1, "Mercado Livre", OrigemPacote.MERCADO_LIVRE),
    SHOPEE(2, "Shopee", OrigemPacote.SHOPEE),
    SALARIO(3, "Salário", null);

    private final int codigo;
    private final String alias;
    private final OrigemPacote origemRemunerada;

    TipoPagamento(int codigo, String alias, OrigemPacote origemRemunerada) {
        this.codigo = codigo;
        this.alias = alias;
        this.origemRemunerada = origemRemunerada;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getAlias() {
        return alias;
    }

    public static TipoPagamento valueOf(Integer codigo){
        for(TipoPagamento tipo : TipoPagamento.values()){
            if(codigo == tipo.getCodigo()) return tipo;
        }

        throw new IllegalArgumentException("Código inválido");
    }
}
