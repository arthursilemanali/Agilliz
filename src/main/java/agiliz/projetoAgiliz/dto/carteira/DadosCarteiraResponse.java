package agiliz.projetoAgiliz.dto.carteira;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record DadosCarteiraResponse(
        Double valorTotalGanhos,
        Integer totalEntregasRealizadas,
        Integer porcentagemDesempenho,
        CarteiraResponse dadosCarteira,
        InformacoesAdicionaisResponse informacoesAdicionais,
        LocalDateTime comecoSemana,
        LocalDateTime finalSemana,
        Integer pacotesFaltaEntregar
) {
}
