package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vigencia_pagamento_aut")
@Getter
public class VigenciaPagamentoAut implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idVigenciaPagamentoAut;

    @CreationTimestamp
    private LocalDateTime dataHoraCriacao;

    @UpdateTimestamp
    private LocalDateTime dataHoraAtualizacao;

    @Setter
    private Integer diaSemana;

    public String getDiaSemanaAlias() {
        return DiaSemana.valueOf(diaSemana).getAlias();
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.of(diaSemana);
    }
}
