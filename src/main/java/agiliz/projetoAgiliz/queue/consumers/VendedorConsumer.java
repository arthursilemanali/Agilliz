package agiliz.projetoAgiliz.queue.consumers;

import agiliz.projetoAgiliz.controllers.VendedorController;
import agiliz.projetoAgiliz.dto.coleta.ColetaMLRequest;
import agiliz.projetoAgiliz.dto.vendedor.VendedorClientTg;
import agiliz.projetoAgiliz.dto.vendedor.VendedorTgToken;
import agiliz.projetoAgiliz.services.ColetaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendedorConsumer {

    @Autowired
    private VendedorController vendedorController;

    @Autowired
    private ColetaService coletaService;

    @RabbitListener(queues = "coletaQueue")
    public void processarColeta(ColetaMLRequest coletaRequest) throws Exception {
        try {
            coletaService.cadastrarML(coletaRequest);
        } catch (Exception e) {
            System.err.println("Erro ao processar coleta: " + e.getMessage());
            throw new Exception("Erro ao processar coleta", e);
        }
    }

    @RabbitListener(queues = "vendedorQueue")
    public void processarVendedor(VendedorClientTg vendedorRequest) throws Exception {
        try {
            vendedorController.cadastrarClienteTg(vendedorRequest);
        } catch (Exception e) {
            System.err.println("Erro ao processar vendedor: " + e.getMessage());
            throw new Exception("Erro ao processar vendedor", e);
        }
    }

    @RabbitListener(queues = "testeQueue")
    public void processarTeste() throws Exception {
        try {
            System.out.println("Fila funcionando!!");
        } catch (Exception e) {
            System.err.println("Erro ao processar teste: " + e.getMessage());
            throw new Exception("Erro ao processar teste", e);
        }
    }

    @RabbitListener(queues = "atualizarTgQueue")
    public void processarAtualizarTg(List<VendedorTgToken> vendedores) throws Exception {
        try {
            vendedorController.atualizarTokens(vendedores);
        } catch (Exception e) {
            System.err.println("Erro ao processar atualização de TG: " + e.getMessage());
            throw new Exception("Erro ao processar atualização de TG", e);
        }
    }
}
