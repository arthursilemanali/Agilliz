package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum DiaSemana {
    SEGUNDA("Segunda"),
    TERCA("Terça"),
    QUARTA("Quarta"),
    QUINTA("Quinta"),
    SEXTA("Sexta"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final static DiaSemana[] ENUMS = values();
    private final String alias;

    DiaSemana(String alias) {
        this.alias = alias;
    }

    public static DiaSemana valueOf(Integer diaSemana) {
        if(diaSemana < 1 || diaSemana > ENUMS.length) {
            throw new IllegalArgumentException("Valor inválido para dia da semana");
        }

        return ENUMS[diaSemana - 1];
    }
}
