package agiliz.projetoAgiliz.models;

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
@Table(name = "pagamento_asaas")
@NoArgsConstructor
public class PagamentoAsaas {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idPagamento;
    private String id;
    private Integer status;
    private Double value;
    private LocalDate paidDate;
    private String paymentMethod;
    private String paymentLink;
    private String barcode;
    private String customer;

    @ManyToOne
    @JoinColumn(name = "fk_vendedor")
    private Vendedor vendedor;

    private LocalDate dataVencimento;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "boleto_id", referencedColumnName = "idBoleto")
    private Boleto boleto;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_id", referencedColumnName = "idNf")
    private NotaFiscal notaFiscal;

    public PagamentoAsaas(Boleto boleto) {
        this.id = boleto.getAsaasId();
        this.status = AsaasEnum.PENDING.getCodigo();
        this.value = boleto.getValue();
        this.paymentMethod = boleto.getBillingType();
        this.paymentLink = boleto.getPaymentLink();
        this.barcode = boleto.getBarcode();
        this.customer = boleto.getCustomer();
        this.boleto = boleto;
    }
}