package agiliz.projetoAgiliz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoManifesto {
    ENTRADA(1, "entrada"),
    SAIDA(2, "saída");

    private final int codigo;
    private final String alias;

    public static TipoManifesto valueOf(int codigo) {
        for(TipoManifesto tipo : TipoManifesto.values()) {
            if(tipo.codigo == codigo) return tipo;
        }

        throw new IllegalArgumentException("Tipo de manifesto com código listado não existe");
    }
}
