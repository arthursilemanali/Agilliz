package agiliz.projetoAgiliz.dto.emissaoPagamento;

import lombok.Data;

@Data
public class EmissaoPagamentoDespesaResponse {
    private String mes;
    private double custoTotal;

    public EmissaoPagamentoDespesaResponse(String mes, double custoTotal) {
        this.mes = mes;
        this.custoTotal = custoTotal;
    }
}
