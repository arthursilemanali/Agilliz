package agiliz.projetoAgiliz.dto.colaborador;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioLoginDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    private String token;
    private String idUsuario;
    private Boolean possuiPermissaoScanner;
    private String fcm_token;
    private String nome;
    List<String> roles;
}
