package  agiliz.projetoAgiliz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tipo_colaborador")
public class TipoColaborador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idTipoColaborador;
    private boolean taxado;
    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoColaborador")
    private List<Pagamento> pagamentos;
}
