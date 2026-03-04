package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transacao")
@NoArgsConstructor
public class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTransacao;
    private Double valor;
    private boolean automatica;

    @CreationTimestamp
    private LocalDateTime dataHoraTransacao;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    public Transacao(Colaborador colaborador) {
        this.valor = colaborador.getCarteira().getSaldo();
        this.colaborador = colaborador;
        this.automatica = true;
    }
}
