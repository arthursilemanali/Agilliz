package agiliz.projetoAgiliz.enums;

import lombok.Getter;

@Getter
public enum PermissoesEnum {
    ROLE_ADM(1, "Administrador", "ROLE_ADMIN"),
    ROLE_MOTOBOY(2, "Motoboy", "ROLE_MOTOBOY"),
    ROLE_FINANCEIRO(3, "Financeiro", "ROLE_FINANCEIRO"),
    ROLE_SCANNER(4, "Scanner", "ROLE_SCANNER");

    private final Integer codigo;
    private final String textoAmigavel;
    private final String roleBanco;

    PermissoesEnum(Integer codigo, String textoAmigavel, String roleBanco) {
        this.codigo = codigo;
        this.textoAmigavel = textoAmigavel;
        this.roleBanco = roleBanco;
    }

    public static String fromCodigo(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Código não pode ser nulo");
        }
        for (PermissoesEnum permissao : values()) {
            if (permissao.codigo.equals(codigo)) {
                return permissao.roleBanco;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}