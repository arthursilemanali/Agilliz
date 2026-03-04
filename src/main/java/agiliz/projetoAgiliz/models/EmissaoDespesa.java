package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.VigenciaDespesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "emissao_despesa")
@AllArgsConstructor
@NoArgsConstructor
public class EmissaoDespesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEmissaoDespesa;

    @ManyToOne
    @JoinColumn(name = "fk_despesa")
    private Despesa despesa;

    @ManyToOne
    @JoinColumn(name = "fk_os")
    private OS os;
    private boolean pago;
    private Double valor;
    private String descricao;

    @CreationTimestamp
    private LocalDate dataEmissao;
    private int vigencia;

    public EmissaoDespesa(Despesa despesa) {
        this.despesa = despesa;
        this.valor = despesa.getValorDespesa();
        this.vigencia = despesa.getVigencia().getCodigo();
        this.descricao = despesa.getDescricaoDespesa();
    }

    public VigenciaDespesa getVigencia() {
        return  VigenciaDespesa.valueOf(vigencia);
    }
}
