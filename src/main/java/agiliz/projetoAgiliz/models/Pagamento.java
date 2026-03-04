package agiliz.projetoAgiliz.models;

import java.util.UUID;

import agiliz.projetoAgiliz.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pagamento")
@NoArgsConstructor
public class Pagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPagamento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_tipo_colaborador")
    private TipoColaborador tipoColaborador;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;
    private Double remuneracao;
    private int tipoPagamento;

    public Pagamento(int tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public TipoPagamento getTipoPagamento() {
        return TipoPagamento.valueOf(tipoPagamento);
    }
}
