package agiliz.projetoAgiliz.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum StatusColeta {
    EM_ESPERA(1, "em espera"),
    EM_ANDAMENTO(2, "em andamento"),
    CONFERINDO(3, "conferindo"),
    CONCLUIDA(4, "concluída"),
    DESPACHADO(5, "despachado"),
    PRONTA_PARA_CONFERENCIA(6, "pronto para conferência");

    private final int codigo;
    private final String alias;

    StatusColeta(int codigo, String alias) {
        this.codigo = codigo;
        this.alias = alias;
    }

    public static StatusColeta fromAlias(String alias){
        for (StatusColeta statusColeta : StatusColeta.values()){
            if(Objects.equals(statusColeta.alias, alias)) return statusColeta;
        }
        throw new IllegalArgumentException("Código inválido para status da coleta");
    }

    public static StatusColeta valueOf(int codigo) {
        for(StatusColeta status : StatusColeta.values()) {
            if(status.codigo == codigo) return status;
        }

        throw new IllegalArgumentException("Código inválido para status da coleta");
    }
}
