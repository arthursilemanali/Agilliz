package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoRequestDTO;
import agiliz.projetoAgiliz.enums.AsaasEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "boleto")
@NoArgsConstructor
public class Boleto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idBoleto;
    private String asaasId;
    private String customer;
    private LocalDate dateCreated;
    private LocalDate dueDate;
    private String billingType;
    private Double value;
    private Integer status;
    private String paymentLink;
    private String barcode;
    private String bankSlipUrl;

    @OneToOne(mappedBy = "boleto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PagamentoAsaas pagamentoAsaas;

    public Boleto(BoletoRequestDTO boletoRequest) {
        this.asaasId = boletoRequest.id();
        this.customer = boletoRequest.customer();
        this.dateCreated = boletoRequest.dateCreated();
        this.dueDate = boletoRequest.dueDate();
        this.billingType = boletoRequest.billingType();
        this.value = boletoRequest.value();
        this.status = AsaasEnum.fromString(boletoRequest.status()).getCodigo();
        this.paymentLink = boletoRequest.paymentLink();
        this.barcode = boletoRequest.barcode();
        this.bankSlipUrl = boletoRequest.bankSlipUrl();
    }
}