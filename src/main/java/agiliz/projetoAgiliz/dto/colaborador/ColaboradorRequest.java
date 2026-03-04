package agiliz.projetoAgiliz.dto.colaborador;

import agiliz.projetoAgiliz.dto.documentos.DocumentoRequest;
import agiliz.projetoAgiliz.dto.pagamento.PagamentoCadastroRequest;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.validation.annotations.CampoUnico;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record ColaboradorRequest(
        @NotBlank String nomeColaborador,
        @NotBlank @CPF String cpf,
        @NotBlank String rg,
        @NotBlank String classeCarteira,
        @NotNull LocalDate dataNascimento,
        @NotBlank
        @CampoUnico(message = "E-mail já cadastrado", fieldName = "emailColaborador", entityClass = Colaborador.class)
        @Email
        String emailColaborador,
        @NotBlank String senhaColaborador,
        @NotNull LocalDate dataAdmissao,
        @NotBlank String telefoneColaborador,
        @NotNull Boolean possuiPermissaoScanner,
        @NotNull @Valid List<PagamentoCadastroRequest> pagamentos,
        @NotNull @Valid DocumentoRequest documentos,
        @NotNull @NotEmpty List<String> roles
) {
}
