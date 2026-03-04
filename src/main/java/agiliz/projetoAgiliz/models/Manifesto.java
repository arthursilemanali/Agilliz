package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.StatusManifesto;
import agiliz.projetoAgiliz.enums.TipoManifesto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "manifesto")
@NoArgsConstructor
public class Manifesto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idManifesto;
    @CreationTimestamp
    private LocalDate dataEmitido;
    private int tipo;
    private int status;

    public Manifesto(int tipo) {
        this.tipo = tipo;
        this.status = 2;
    }

    public TipoManifesto getTipo() {
        return TipoManifesto.valueOf(tipo);
    }

    public StatusManifesto getStatus() {
        return StatusManifesto.valueOf(status);
    }
}
