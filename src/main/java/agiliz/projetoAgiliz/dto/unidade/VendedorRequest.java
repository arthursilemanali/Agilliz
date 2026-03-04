package agiliz.projetoAgiliz.dto.unidade;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaCadastro;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalTime;
import java.util.List;

public record VendedorRequest(
        @NotBlank String nomeVendedor,
        @NotBlank String rua,
        @NotBlank String cep,
        @PositiveOrZero @NotNull Integer numero,
        @NotBlank String telefoneVendedor,
        @NotBlank @CNPJ String cnpj,
        LocalTime horarioCorte,
        @NotNull @Valid @NotEmpty List<PrecificacaoZonaCadastro> zonas
) {
}
