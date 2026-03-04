package agiliz.projetoAgiliz.clients;

import agiliz.projetoAgiliz.dto.ml.ShipmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ml-integration", url = "${ms.roteirizacao.url}")
public interface MlClient {
    @GetMapping("/integration/{clientId}/shipment/{shipmentId}")
    ResponseEntity<ShipmentResponse> getStatusPacote(
            @PathVariable String clientId,
            @PathVariable Integer shipmentId
    );
}
