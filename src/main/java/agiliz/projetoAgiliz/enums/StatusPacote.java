package agiliz.projetoAgiliz.enums;


import lombok.Getter;

@Getter
public enum StatusPacote {
    A_CAMINHO(1, "ready_to_ship", "A caminho"),
    CONCLUIDO(2, "delivered", "Concluído"),
    CANCELADO(3, "cancelled", "Cancelado"),
    DEVOLVIDO(4, "returned", "Devolvido"),
    AUSENTE(5, "absent", "Ausente"),
    EM_ESPERA(6, "on_hold", "Em espera");

    private final int codigo;
    private final String alias;
    private final String descricao;

    StatusPacote(int codigo, String alias, String descricao) {
        this.codigo = codigo;
        this.alias = alias;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPacote valueOf(int codigo) {
        for(StatusPacote tipo : StatusPacote.values()) {
            if(codigo == tipo.getCodigo()) return tipo;
        }

        throw new IllegalArgumentException("Código inválido");
    }
}
