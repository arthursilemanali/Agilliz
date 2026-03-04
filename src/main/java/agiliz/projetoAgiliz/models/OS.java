package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.StatusOS;
import agiliz.projetoAgiliz.enums.TipoOS;
import jakarta.persistence.*;
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
@NoArgsConstructor
@Table(name = "os")
public class OS {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idOS;
    private Double valor;
    @CreationTimestamp
    private LocalDate dataEmissao;
    private int status;
    private int tipo;

    @OneToMany(mappedBy = "os", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmissaoPagamento> emissaoPagamentos;

    public StatusOS getStatus() {
        return StatusOS.valueOf(status);
    }

    public TipoOS getTipo() {
        return TipoOS.valueOf(tipo);
    }
}
