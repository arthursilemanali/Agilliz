package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoResponseNoVendedor;
import agiliz.projetoAgiliz.enums.TipoPagamento;
import agiliz.projetoAgiliz.enums.TipoZona;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "zona")
@NoArgsConstructor
@ToString
public class Zona implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idZona;

    private String nomeZona;
    private Integer limiteSuperiorCEP;
    private Integer limiteInferiorCEP;
    private int tipoZona;

    @OneToMany(mappedBy = "zona")
    private List<Pacote> pacotes;

    public TipoZona getTipoZona() {
        return TipoZona.valueOf(tipoZona);
    }
}
