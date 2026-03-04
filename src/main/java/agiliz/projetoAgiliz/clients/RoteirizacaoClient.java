package agiliz.projetoAgiliz.clients;

import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.RoteirizacaoRequest;
import agiliz.projetoAgiliz.dto.roteirizacao.msRoteirizacao.RoteirizacaoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "roteirizacao", url = "${ms.roteirizacao.url}")
public interface RoteirizacaoClient {

    @PostMapping
    RoteirizacaoResponse getRoteirizacao(
        @RequestBody RoteirizacaoRequest request,
        @RequestParam String zoneId
    );
}
