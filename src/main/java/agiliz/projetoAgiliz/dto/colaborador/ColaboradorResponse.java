package agiliz.projetoAgiliz.dto.colaborador;

import agiliz.projetoAgiliz.dto.carteira.CarteiraResponse;
import agiliz.projetoAgiliz.dto.carteira.CarteiraResponseNoColaborador;
import agiliz.projetoAgiliz.dto.documentos.DocumentoResponse;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoCadastroResponse;
import agiliz.projetoAgiliz.models.Colaborador;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ColaboradorResponse(
        UUID idColaborador,
        String nomeColaborador,
        String cpf,
        String rg,
        String classeCarteira,
        LocalDate dataNascimento,
        String emailColaborador,
        LocalDate dataAdmissao,
        String telefoneColaborador,
        Boolean possuiPermissaoScanner,
        List<PagamentoCadastroResponse> pagamentos,
        DocumentoResponse documentos,
        CarteiraResponseNoColaborador dadosCarteira
) {
    public ColaboradorResponse(Colaborador colaborador) {
        this(
                colaborador.getIdColaborador(),
                colaborador.getNomeColaborador(),
                colaborador.getCpf(),
                colaborador.getRg(),
                colaborador.getClasseCarteira(),
                colaborador.getDataNascimento(),
                colaborador.getEmailColaborador(),
                colaborador.getDataAdmissao(),
                colaborador.getTelefoneColaborador(),
                colaborador.getPossuiPermissaoScanner(),
                colaborador.getPagamentos() != null
                        ? colaborador.getPagamentos().stream()
                        .map(PagamentoCadastroResponse::new)
                        .collect(Collectors.toList())
                        : null,
                colaborador.getDocumentos() != null
                        ? new DocumentoResponse(colaborador.getDocumentos())
                        : null,
                colaborador.getCarteira() != null
                ? new CarteiraResponseNoColaborador(colaborador.getCarteira())
                : null
        );
    }
}
