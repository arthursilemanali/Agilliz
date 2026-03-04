package agiliz.projetoAgiliz.dto.pacote;

import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.Vendedor;

import java.util.List;
import java.util.stream.Collectors;

public record ColetasResponseEmEspera(
        String nomeCliente,
        Long totalPacotes,
        List<PacoteResponseEmEspera> listaEnvios
) {
    public ColetasResponseEmEspera(Vendedor cliente) {
        this(
                cliente.getNomeVendedor(),
                cliente.getPacotes().stream().filter(pct ->
                        pct.getStatus()
                        .getCodigo()==6).count(),
                cliente.getPacotes().stream().map(
                        PacoteResponseEmEspera::new
                ).toList()
        );
    }
}
