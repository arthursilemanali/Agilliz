package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "setor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Setor implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSetor;
    private String limiteInferiorCep;
    private String limiteSuperiorCep;

    @OneToOne
    @JoinColumn(name = "fk_lider")
    private Colaborador liderSetor;

    @OneToMany(mappedBy = "setor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembroSetor> membros;

    @OneToMany(mappedBy = "setor", fetch = FetchType.LAZY)
    private List<Pacote> pacotes;

    private Double valorBonificacao;
}
