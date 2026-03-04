package agiliz.projetoAgiliz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusManifesto {
    CONFERIDO(1, "conferido"),
    PENDENTE(2, "pendente");

    private final int codigo;
    private final String alias;

    public static StatusManifesto valueOf(int codigo) {
        for (StatusManifesto status : StatusManifesto.values()) {
            if(status.codigo == codigo) return status;
        }

        throw new IllegalArgumentException("Status de manifesto com código informado não existe");
    }
}
