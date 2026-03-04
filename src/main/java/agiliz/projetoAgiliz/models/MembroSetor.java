package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "membro_setor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MembroSetor implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idMembroSetor;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "fk_setor")
    private Setor setor;


    @CreationTimestamp
    private LocalDate dataAlocacao;
}
