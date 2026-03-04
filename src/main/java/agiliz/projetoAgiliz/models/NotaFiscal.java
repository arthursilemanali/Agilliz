package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.asaas.nf.NfRequestDTO;
import agiliz.projetoAgiliz.enums.StatusNfEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "nf")
@NoArgsConstructor
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idNf;

    private String id;
    private Integer status;
    private String customer;
    private String payment;
    private String installment;
    private String type;
    private String statusDescription;
    private String serviceDescription;
    private String pdfUrl;
    private String xmlUrl;
    private String rpsSerie;
    private String rpsNumber;
    private String number;
    private String validationCode;
    private Double value;
    private Double deductions;
    private LocalDate effectiveDate;
    private String observations;
    private String estimatedTaxesDescription;
    private String externalReference;
    private Boolean retainIss;
    private Double cofins;
    private Double csll;
    private Double inss;
    private Double ir;
    private Double pis;
    private Double iss;
    private String municipalServiceId;
    private String municipalServiceCode;
    private String municipalServiceName;

    @OneToOne(mappedBy = "notaFiscal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PagamentoAsaas pagamentoAsaas;

    public NotaFiscal(NfRequestDTO nfRequestDTO) {
        this.id = nfRequestDTO.id();
        this.status = StatusNfEnum.fromName(nfRequestDTO.status()).getCodigo();
        this.customer = nfRequestDTO.customer();
        this.payment = nfRequestDTO.payment();
        this.installment = nfRequestDTO.installment();
        this.type = nfRequestDTO.type();
        this.statusDescription = nfRequestDTO.statusDescription();
        this.serviceDescription = nfRequestDTO.serviceDescription();
        this.pdfUrl = nfRequestDTO.pdfUrl();
        this.xmlUrl = nfRequestDTO.xmlUrl();
        this.rpsSerie = nfRequestDTO.rpsSerie();
        this.rpsNumber = nfRequestDTO.rpsNumber();
        this.number = nfRequestDTO.number();
        this.validationCode = nfRequestDTO.validationCode();
        this.value = nfRequestDTO.value();
        this.deductions = nfRequestDTO.deductions();
        this.effectiveDate = nfRequestDTO.effectiveDate();
        this.observations = nfRequestDTO.observations();
        this.estimatedTaxesDescription = nfRequestDTO.estimatedTaxesDescription();
        this.externalReference = nfRequestDTO.externalReference();
        this.retainIss = nfRequestDTO.retainIss();
        this.cofins = nfRequestDTO.cofins();
        this.csll = nfRequestDTO.csll();
        this.inss = nfRequestDTO.inss();
        this.ir = nfRequestDTO.ir();
        this.pis = nfRequestDTO.pis();
        this.iss = nfRequestDTO.iss();
        this.municipalServiceId = nfRequestDTO.municipalServiceId();
        this.municipalServiceCode = nfRequestDTO.municipalServiceCode();
        this.municipalServiceName = nfRequestDTO.municipalServiceName();
    }
}