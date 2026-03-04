package agiliz.projetoAgiliz.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import agiliz.projetoAgiliz.dto.pacote.PacoteMLRequest;
import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import agiliz.projetoAgiliz.enums.OrigemPacote;
import agiliz.projetoAgiliz.enums.StatusPagamentoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import agiliz.projetoAgiliz.enums.StatusPacote;
import agiliz.projetoAgiliz.enums.TipoPacote;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pacotes")
public class Pacote implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPacote;
    private int tipo;
    private int status;
    private int statusPagamento;
    private LocalDateTime dataColeta;
    private LocalDateTime dataEntrega;
    private int origem;
    private int quantidadeTentativasEntrega;
    private String observacao;

    @Embedded
    private Coordenada coordenadas;

    @ManyToOne
    @JoinColumn(name = "fk_zona")
    private Zona zona;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_destinatario")
    private Destinatario destinatario;

    @ManyToOne
    @JoinColumn(name = "fk_vendedor")
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "fk_coleta")
    private Coleta coleta;

    private String idEcommerce;

    @ManyToOne
    @JoinColumn(name = "fk_setor")
    private Setor setor;

    public Pacote(int status) {
        this.status = status;
    }

    public Pacote(PacoteMLRequest pacoteMLRequest, Vendedor vendedor, Zona zona, Coleta coleta) {
        this.tipo = 2;
        this.status = 6;
        this.origem = 1;
        this.destinatario = new Destinatario(pacoteMLRequest.destination());
        this.vendedor = vendedor;
        this.coordenadas = new Coordenada(pacoteMLRequest.destination().latitude()
                , pacoteMLRequest.destination().latitude());
        this.idEcommerce = pacoteMLRequest.id().toString();
        this.zona = zona;
        this.coleta = coleta;
    }

    public StatusPacote getStatus() {
        return StatusPacote.valueOf(status);
    }

    public TipoPacote getTipo() {
        return TipoPacote.valueOf(tipo);
    }

    public OrigemPacote getOrigem() {
        return OrigemPacote.valueOf(origem);
    }

    @Override
    public String toString() {
        return "Pacote{" +
                "idPacote=" + idPacote +
                ", tipo=" + tipo +
                ", status=" + status +
                ", zona=" + zona +
                ", colaborador=" + colaborador +
                ", destinatario=" + destinatario +
                ", vendedor=" + vendedor +
                '}';
    }
}
