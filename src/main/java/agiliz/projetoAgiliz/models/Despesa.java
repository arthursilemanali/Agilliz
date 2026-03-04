package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.TipoDespesa;
import agiliz.projetoAgiliz.enums.VigenciaDespesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "despesa")
public class Despesa implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private UUID idDespesa;
    private int tipoDespesa;
    private int vigencia;
    private double valorDespesa;
    private Integer diaVigencia;

    @CreationTimestamp
    private LocalDate dataCadastrado;

    private String descricaoDespesa;

    @ManyToOne
    @JoinColumn(name = "fk_veiculo")
    private Veiculo veiculo;

    @OneToMany(mappedBy = "despesa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmissaoDespesa> emissoes;

    // Ver como será feito a questão do anexo

    public String getCronExp() {
        VigenciaDespesa vigencia = getVigencia();

        if(vigencia != VigenciaDespesa.MENSAL) return vigencia.getCronExpression();
        if(diaVigencia > 27) return vigencia.getCronExpression().formatted("28-31");
        return vigencia.getCronExpression().formatted(diaVigencia);
    }

    public TipoDespesa getTipoDespesa() {
        return TipoDespesa.valueOf(tipoDespesa);
    }

    public VigenciaDespesa getVigencia() {
        return VigenciaDespesa.valueOf(vigencia);
    }
}
