package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "EnderecoFinal")
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoFinal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEnderecoFinal;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    private String apelido;
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String estado;

    @Embedded
    private Coordenada localizacao;
}
