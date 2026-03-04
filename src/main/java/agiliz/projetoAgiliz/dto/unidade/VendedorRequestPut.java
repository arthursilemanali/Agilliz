package agiliz.projetoAgiliz.dto.unidade;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoResponseNoVendedor;
import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaCadastro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record VendedorRequestPut(
        UUID idVendedor,
        @NotBlank String nomeVendedor,
        @NotBlank String rua,
        @NotBlank String cep,
        @PositiveOrZero @NotNull Integer numero,
        @NotBlank String telefoneVendedor,
        @NotBlank @CNPJ String cnpj,
        LocalTime horarioCorte,
        @NotNull @Valid List<PrecificacaoResponseNoVendedor> zonas,
        @Email String email,
        String nomeFantasia,
        String bairro,
        String cidade,
        String estado
) {
}
