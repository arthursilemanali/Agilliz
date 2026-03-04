package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carteira")
public class Carteira implements Serializable {
    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCarteira;

    @OneToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    private int numeroConta;
    private String chavePix;
    private String agencia;
    private double saldo;

    public void atualizarSaldo(Double valor) {
        saldo += valor;
    }

    public Carteira(UUID idCarteira, int numeroConta, String chavePix, String agencia, double saldo) {
        this.idCarteira = idCarteira;
        this.numeroConta = numeroConta;
        this.chavePix = chavePix;
        this.agencia = agencia;
        this.saldo = saldo;
    }
}
