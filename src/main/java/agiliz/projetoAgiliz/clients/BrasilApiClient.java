package agiliz.projetoAgiliz.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "brasilApi", url = "https://brasilapi.com.br/api/")
public interface BrasilApiClient {



}
