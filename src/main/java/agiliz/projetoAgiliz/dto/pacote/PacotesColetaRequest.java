package agiliz.projetoAgiliz.dto.pacote;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PacotesColetaRequest(
   @NotNull UUID fkColeta,
   @NotNull UUID fkColaborador,
   @NotNull List<PacoteRequest> pacotes
) {}
