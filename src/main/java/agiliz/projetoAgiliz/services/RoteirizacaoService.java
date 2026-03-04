package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.clients.RoteirizacaoClient;
import agiliz.projetoAgiliz.dto.roteirizacao.Coordenada;
import agiliz.projetoAgiliz.dto.roteirizacao.Parada;
import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.RoteirizacaoRequest;
import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.RoteirizacaoResponse;
import agiliz.projetoAgiliz.models.Pacote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoteirizacaoService {
    private static final Logger logger = LoggerFactory.getLogger(RoteirizacaoService.class);

    @Autowired
    private RoteirizacaoClient roteirizacaoClient;

    @Value("${zone.id}")
    private String zoneId;

    public RoteirizacaoResponse getRoteirizacao(List<Pacote> pacotes, Coordenada inicio, Coordenada fim) {
        List<Parada> paradas = pacotes.stream()
            .map(Parada::new)
            .toList();

        RoteirizacaoRequest request = new RoteirizacaoRequest(paradas, inicio, fim);
        return roteirizacaoClient.getRoteirizacao(request, zoneId);
    }
}
