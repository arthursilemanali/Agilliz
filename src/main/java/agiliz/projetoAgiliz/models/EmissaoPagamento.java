package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.setor.BonificacaoLider;
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
@Table(name = "emissao_pagamento")
@NoArgsConstructor
public class EmissaoPagamento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idEmissaoPagamento;

    @ManyToOne
    @JoinColumn(name = "fk_colaborador")
    private Colaborador colaborador;

    @ManyToOne
    @JoinColumn(name = "fk_os")
    private OS os;
    @CreationTimestamp
    private LocalDate data;
    private Double valor;
    private int statusPagamento;
    private boolean bonus;

    public EmissaoPagamento(Colaborador colaborador, Double valor) {
        this.colaborador = colaborador;
        this.valor = valor;
        this.statusPagamento = 2;
    }

    public EmissaoPagamento(Pagamento pagamento) {
        this.colaborador = pagamento.getColaborador();
        this.valor = pagamento.getRemuneracao();
        this.statusPagamento = 3;
    }

    public EmissaoPagamento(
        UUID idEmissaoPagamento,
        Colaborador colaborador,
        LocalDate data,
        Double valor
    ) {
        this.idEmissaoPagamento = idEmissaoPagamento;
        this.colaborador = colaborador;
        this.data = data;
        this.valor = valor;
    }

    public EmissaoPagamento(BonificacaoLider bonificacaoLider) {
        this.colaborador = bonificacaoLider.lider();
        this.valor = bonificacaoLider.totalBonificacao();
        this.statusPagamento = 2;
        this.bonus = true;
    }

    public EmissaoPagamento(Transacao transacao) {
        this.colaborador = transacao.getColaborador();
        this.valor = transacao.getValor();
        this.statusPagamento = 2;
    }
}
