package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum TipoVeiculo {
    CARRO(1, "carro"),
    MOTO(2, "moto"),
    CAMINHAO(3, "caminhão");

    private final int codigo;
    private final String alias;

    TipoVeiculo(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    public static TipoVeiculo valueOf(int codigo) {
        for(TipoVeiculo tipo : TipoVeiculo.values()) {
            if(tipo.codigo == codigo) return tipo;
        }

        throw new IllegalArgumentException("Código de tipo de veículo inválido");
    }
}
