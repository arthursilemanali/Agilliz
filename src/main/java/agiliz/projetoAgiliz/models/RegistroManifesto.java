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
@Table
@NoArgsConstructor
public class RegistroManifesto implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRegistroManifesto;

    @CreationTimestamp
    private LocalDate dataResgistro;

    @ManyToOne
    @JoinColumn(name = "fk_manifesto")
    private Manifesto manifesto;

    @ManyToOne
    @JoinColumn(name = "fk_pacote")
    private Pacote pacote;

    public RegistroManifesto(Manifesto manifesto, Pacote pacote) {
        this.manifesto = manifesto;
        this.pacote = pacote;
    }
}
