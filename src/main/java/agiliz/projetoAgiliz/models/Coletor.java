package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "coletor")
public class Coletor implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColetor;

    @CreationTimestamp
    private LocalDate dataColeta;
    private Integer pacotesColetados;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "fk_coleta")
    private Coleta coleta;

    private boolean conferente;

    public Coletor(Integer pacotesColetados) {
        this.pacotesColetados = pacotesColetados;
    }
}
