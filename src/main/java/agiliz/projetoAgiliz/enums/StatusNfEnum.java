package agiliz.projetoAgiliz.enums;

import java.util.Arrays;

public enum StatusNfEnum {
    SCHEDULED(1, "Agendada"),
    AUTHORIZED(2, "Autorizada"),
    PROCESSING_CANCELLATION(3, "Cancelamento em processamento"),
    CANCELED(4, "Cancelada"),
    CANCELLATION_DENIED(5, "Cancelamento negado");

    private final int codigo;
    private final String descricao;

    StatusNfEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static StatusNfEnum fromName(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status não encontrado para o nome: " + name));
    }

    public static StatusNfEnum fromCodigo(int codigo) {
        return Arrays.stream(values())
                .filter(status -> status.getCodigo() == codigo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status não encontrado para o código: " + codigo));
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
