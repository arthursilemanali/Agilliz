package agiliz.projetoAgiliz.dto.veiculo;

import agiliz.projetoAgiliz.models.Veiculo;

import java.util.UUID;

public record VeiculoResponse(
        UUID idVeiculo,
        String tipoVeiculo,
        String proprietario,
        String placa,
        String marca,
        String modelo

) {
    public VeiculoResponse(Veiculo veiculo) {
        this(
                veiculo.getIdVeiculo(),
                veiculo.getTipoVeiculo().getAlias(),
                veiculo.getColaborador() == null
                        ? " "
                        : veiculo.getColaborador().getNomeColaborador(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo()
        );
    }
}
