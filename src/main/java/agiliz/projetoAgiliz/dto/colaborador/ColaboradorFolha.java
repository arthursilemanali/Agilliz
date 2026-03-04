package agiliz.projetoAgiliz.dto.colaborador;

import agiliz.projetoAgiliz.dto.pagamento.PagamentoFolha;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.EmissaoPagamento;

import java.util.List;
import java.util.UUID;

public record ColaboradorFolha(
    UUID idColaborador,
    String nome,
    String cpf,
    String rg,
    List<PagamentoFolha> emissoes
) {
    public ColaboradorFolha(Colaborador colaborador) {
        this(
            colaborador.getIdColaborador(),
            colaborador.getNomeColaborador(),
            colaborador.getCpf(),
            colaborador.getRg(),
            colaborador.getEmissoesPagamento().stream().map(PagamentoFolha::new).toList()
        );
    }

    public Double getTotalColaborador() {
        return emissoes.stream()
            .mapToDouble(PagamentoFolha::valor)
            .sum();
    }
}
