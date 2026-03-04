package agiliz.projetoAgiliz.dto.pacote;

import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import agiliz.projetoAgiliz.dto.setor.SetorResponse;
import agiliz.projetoAgiliz.dto.vendedor.VendedorResponse;
import agiliz.projetoAgiliz.models.Pacote;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PacoteResponse(
        UUID idPacote,
        String tipo,
        String status,
        LocalDateTime dataColeta,
        LocalDateTime dataEntrega,
        String origem,
        Integer quantidadeTentativasEntrega,
        String observacao,
        DestinatarioResponse destinatario,
        VendedorResponse vendedor,
        ColaboradorResponse colaborador,
        String idEcommerce,
        SetorResponse setor
) {
    public PacoteResponse(Pacote p) {
        this(
                p.getIdPacote(),
                p.getTipo().getAlias(),
                p.getStatus().getAlias(),
                p.getDataColeta(),
                p.getDataEntrega(),
                p.getOrigem().getAlias(),
                p.getQuantidadeTentativasEntrega(),
                p.getObservacao(),
                p.getDestinatario() == null ? null : new DestinatarioResponse(p.getDestinatario()),
                p.getVendedor() == null ? null : new VendedorResponse(p.getVendedor()),
                p.getColaborador() == null ? null : new ColaboradorResponse(p.getColaborador()),
                p.getIdEcommerce() == null ? null : p.getIdEcommerce(),
                p.getSetor() == null ? null : new SetorResponse(p.getSetor())
        );
    }
}
