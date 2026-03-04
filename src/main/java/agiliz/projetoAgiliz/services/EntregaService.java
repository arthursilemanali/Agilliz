package agiliz.projetoAgiliz.services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import agiliz.projetoAgiliz.dto.PacotePorcentagemDTO;
import agiliz.projetoAgiliz.dto.dashEntregas.RankingEntregasDTO;

@Service
public class EntregaService {

    @Autowired
    private PacoteService pacoteService;

    @Autowired
    private ColaboradorService colaboradorService;

    private static final Logger logger = LoggerFactory.getLogger(EntregaService.class);

    public List<PacotePorcentagemDTO> listarPorcentagem(){
        logger.info("[EntregaService.listarPorcentagem] Início da listagem de pacotes com porcentagem");
        List<PacotePorcentagemDTO> porcentagens = pacoteService.listarPacotesPorcentagem();
        logger.info("[EntregaService.listarPorcentagem] Listagem de pacotes com porcentagem concluída com sucesso");
        return porcentagens;
    }

    public List<RankingEntregasDTO> listarRankingEntregas(LocalDateTime start, LocalDateTime end){
        logger.info("[EntregaService.listarRankingEntregas] Início da listagem do ranking de entregas");
        List<RankingEntregasDTO> ranking = pacoteService.listarRankingEntregas(start, end);
        logger.info("[EntregaService.listarRankingEntregas] Listagem do ranking de entregas concluída com sucesso");
        return ranking;
    }
}
