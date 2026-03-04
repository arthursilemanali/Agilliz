package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Veiculo")
public class Veiculo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idVeiculo;
    private int tipoVeiculo;
    private String placa;
    private String marca;
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_colaborador")
    private Colaborador colaborador;

    public Veiculo(UUID idVeiculo, int tipoVeiculo, String placa, String marca, String modelo) {
        this.idVeiculo = idVeiculo;
        this.tipoVeiculo = tipoVeiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
    }

    public TipoVeiculo getTipoVeiculo() {
        return TipoVeiculo.valueOf(tipoVeiculo);
    }
}
