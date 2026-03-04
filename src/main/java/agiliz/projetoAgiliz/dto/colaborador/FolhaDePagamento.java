package agiliz.projetoAgiliz.dto.colaborador;

import java.util.List;

public record FolhaDePagamento(List<ColaboradorFolha> colaboradores) {
    public Double getTotalFolhaPagamento() {
        return colaboradores.stream()
            .mapToDouble(ColaboradorFolha::getTotalColaborador)
            .sum();
    }
}
