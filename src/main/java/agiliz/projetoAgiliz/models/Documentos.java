package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Documentos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDocumento;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColaboradorResidencia;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColaboradorCTPS;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColaboradorHabilitacao;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColaboradorReservista;

    @OneToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;
}
