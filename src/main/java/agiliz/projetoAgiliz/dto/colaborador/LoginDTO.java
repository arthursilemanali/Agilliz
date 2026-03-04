package agiliz.projetoAgiliz.dto.colaborador;

import com.fasterxml.jackson.annotation.JsonIgnore;

import agiliz.projetoAgiliz.models.Colaborador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class LoginDTO {
    private String emailColaborador;
    @JsonIgnore
    private String senhaColaborador;
    private UUID idColaborador;
    private Boolean possuiPermissaoScanner;
    private String fcm_token;
    
    public LoginDTO(Colaborador colaborador) {
        this.emailColaborador = colaborador.getEmailColaborador();
        this.senhaColaborador = colaborador.getSenhaColaborador();
        this.idColaborador = colaborador.getIdColaborador();
        this.possuiPermissaoScanner = colaborador.getPossuiPermissaoScanner();
        this.fcm_token = colaborador.getFcm_token();
    }

    
}
