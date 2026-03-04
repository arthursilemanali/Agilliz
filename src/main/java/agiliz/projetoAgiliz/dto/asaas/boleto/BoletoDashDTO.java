package agiliz.projetoAgiliz.dto.asaas.boleto;

import agiliz.projetoAgiliz.models.Boleto;

import java.time.LocalDate;

public record BoletoDashDTO(
        LocalDate dueDate,
        Double value,
        String cliente,
        Integer status
        ) {
    public BoletoDashDTO(Boleto boleto){
        this(
             boleto.getDueDate(),
             boleto.getValue(),
             boleto.getCustomer(),
            boleto.getStatus()
        );
    }
}
