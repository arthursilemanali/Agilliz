package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.StatusColeta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coleta")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Coleta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColeta;
    private Integer romaneio;
    private Integer statusColeta;

    @ManyToOne
    @JoinColumn(name = "fk_vendedor")
    private Vendedor vendedor;

    @OneToMany(mappedBy = "coleta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pacote> pacotes;

    @OneToMany(mappedBy = "coleta", fetch = FetchType.LAZY)
    private List<Coletor> coletores;

    @CreationTimestamp
    private LocalDateTime diaHoraRegistro;

    public Coleta(Integer romaneio) {
        this.romaneio = romaneio;
        this.statusColeta = 1;
    }

    public StatusColeta getStatusColeta() {
        return StatusColeta.valueOf(statusColeta);
    }
}
