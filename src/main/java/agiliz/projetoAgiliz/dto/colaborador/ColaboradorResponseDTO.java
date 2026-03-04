package agiliz.projetoAgiliz.dto.colaborador;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColaboradorResponseDTO {
    private String nomeColaborador;
    private String cpf;
    private String rg;
    private String classeCarteira;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;
    private String telefoneColaborador;
}
