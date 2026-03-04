package agiliz.projetoAgiliz.dto.colaborador;

import agiliz.projetoAgiliz.dto.pagamento.PagamentoCadastroRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

public record ColaboradorRequestPut(
        @NotBlank String nomeColaborador,
        @NotBlank @CPF String cpf,
        @NotBlank String rg,
        @NotBlank String classeCarteira,
        @NotNull LocalDate dataNascimento,
        @Email String emailColaborador,
        String senhaColaborador,
        @NotNull LocalDate dataAdmissao,
        @NotBlank String telefoneColaborador,
        @NotNull Boolean possuiPermissaoScanner,
        @NotNull List<PagamentoCadastroRequest> pagamentos) {

}
