package agiliz.projetoAgiliz.dto.emissaoDespesa;

import agiliz.projetoAgiliz.dto.OS.OSResponse;
import agiliz.projetoAgiliz.dto.despesa.DespesaResponse;
import agiliz.projetoAgiliz.models.EmissaoDespesa;

import java.time.LocalDate;
import java.util.UUID;

public record EmissaoDespesaResponse(
    UUID idEmissaoDespesa,
    DespesaResponse despesa,
    OSResponse os,
    String pago,
    Double valor,
    LocalDate dataEmissao
) {

    public EmissaoDespesaResponse(EmissaoDespesa emissao) {
        this(
            emissao.getIdEmissaoDespesa(),
            new DespesaResponse(emissao.getDespesa()),
            new OSResponse(emissao.getOs()),
            emissao.isPago() ? "Sim" : "Não",
            emissao.getValor(),
            emissao.getDataEmissao()
        );
    }

}
