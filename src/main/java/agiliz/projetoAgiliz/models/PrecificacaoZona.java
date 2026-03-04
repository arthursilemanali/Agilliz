package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoResponseNoVendedor;
import agiliz.projetoAgiliz.dto.unidade.VendedorRequestPut;
import agiliz.projetoAgiliz.enums.TipoZona;
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
@Table(name = "precificacaoZona")
@AllArgsConstructor
@NoArgsConstructor
public class PrecificacaoZona implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPrecificacaoZona;

    @ManyToOne
    @JoinColumn(name = "fk_vendedor")
    private Vendedor vendedor;

    private Double preco;

    private int tipoZona;

    public TipoZona getTipoZona() {
        return TipoZona.valueOf(tipoZona);
    }

    public PrecificacaoZona(Vendedor vendedor, PrecificacaoResponseNoVendedor precificacaoDTO){
        this(
            precificacaoDTO.idPrecificacaoZona(),
            vendedor,
            precificacaoDTO.preco(),
            precificacaoDTO.tipoZona()
        );
    }
}
