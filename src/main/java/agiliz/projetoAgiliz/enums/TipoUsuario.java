package agiliz.projetoAgiliz.enums;

public enum TipoUsuario {
    ADMIN(1, "Administrador"),
    GESTOR(2, "Gestor"),
    FINANCEIRO(3, "Financeiro"),
    CONFERENTE(4, "Conferente"),
    ENTREGADOR(5, "Entregador"),
    COLETOR(6, "Coletor");

    private final int codigo;
    private final String descricao;

    TipoUsuario(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoUsuario porCodigo(int codigo) {
        for (TipoUsuario tipo : values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}