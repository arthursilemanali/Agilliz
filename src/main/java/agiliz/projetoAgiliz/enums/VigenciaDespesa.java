package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum VigenciaDespesa {
    MENSAL(1, "Mensal", "0 0 9 %s * *"),
    SEMANAL(2, "Semanal", "0 0 9 * * MON"),
    DIARIO(3, "Diário", "0 0 9 * * *"),
    UNICO(4, "Único",null);

    private final int codigo;
    private final String alias;
    private final String cronExpression;

    VigenciaDespesa(int codigo, String alias, String cronExpression) {
        this.codigo = codigo;
        this.alias = alias;
        this.cronExpression = cronExpression;
    }

    public static VigenciaDespesa valueOf(int codigo) {
        for(VigenciaDespesa vigencia : VigenciaDespesa.values()) {
            if(vigencia.codigo == codigo) return vigencia;
        }

        throw new IllegalArgumentException("Código de vigência de despesa inválido");
    }
}
