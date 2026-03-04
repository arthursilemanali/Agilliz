package agiliz.projetoAgiliz.dto.enderecoFinal;

import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import agiliz.projetoAgiliz.models.EnderecoFinal;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnderecoFinalResponse(
        UUID idEnderecoFinal,
        String apelido,
        String cep,
        String rua,
        String numero,
        String complemento,
        Coordenada coordenada,
        String bairro,
        String estado
) {
    public EnderecoFinalResponse(EnderecoFinal endereco) {
        this(
                endereco.getIdEnderecoFinal(),
                endereco.getApelido(),
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getLocalizacao(),
                endereco.getBairro(),
                endereco.getEstado()
        );
    }
}
