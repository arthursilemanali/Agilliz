package agiliz.projetoAgiliz.configs.scheduler;

import agiliz.projetoAgiliz.services.AgendamentoService;
import agiliz.projetoAgiliz.services.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AplicacaoStartupConfig implements ApplicationListener<ApplicationReadyEvent> {
    private final AgendamentoService agendamentoService;
    private final PagamentoService pagamentoService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        agendamentoService.agendarTarefa(
            "SALARIOS",
            pagamentoService::cadastrarSalarios,
            "0 0 9 * * *"
        );

        agendamentoService.agendarTarefa(
                "BONIFICACOES",
                pagamentoService::bonificarLideres,
                "0 0 9 * * *"
        );

        agendamentoService.agendarTarefa(
                "PAGAMENTOS_AUT",
                pagamentoService::realizarPagamentoAut,
                "0 0 9 * * *"
        );
    }
}
