package agiliz.projetoAgiliz.dto.asaas.nf;

import java.time.LocalDate;
import java.util.UUID;

public record NfRequestDTO(
        UUID idNf,
        String id,
        String status,
        String customer,
        String payment,
        String installment,
        String type,
        String statusDescription,
        String serviceDescription,
        String pdfUrl,
        String xmlUrl,
        String rpsSerie,
        String rpsNumber,
        String number,
        String validationCode,
        Double value,
        Double deductions,
        LocalDate effectiveDate,
        String observations,
        String estimatedTaxesDescription,
        String externalReference,
        Boolean retainIss,
        Double cofins,
        Double csll,
        Double inss,
        Double ir,
        Double pis,
        Double iss,
        String municipalServiceId,
        String municipalServiceCode,
        String municipalServiceName
) {
}
