package agiliz.projetoAgiliz.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
    private static final Logger logger = LoggerFactory.getLogger(AgendamentoService.class);
    private final Map<String, ScheduledFuture<?>> agendamentos = new ConcurrentHashMap<>();
    private final TaskScheduler scheduler;

    @Value("${zone.id}")
    private String ZONE_ID;

    public void agendarTarefa(String id, Runnable tarefa, String cronExp) {
        logger.info("[AgendamentoService.agendarTarefa] Agendando tarefa com ID: {}", id);

        agendamentos.put(id, scheduler.schedule(tarefa, new CronTrigger(cronExp, ZoneId.of(ZONE_ID))));

        logger.info("[AgendamentoService.agendarTarefa] Tarefa com ID: {} agendada com sucesso", id);
    }

    public void cancelarTarefa(String id) {
        logger.info("[AgendamentoService.cancelarTarefa] Solicitando cancelamento da tarefa com ID: {}", id);
        ScheduledFuture<?> tarefa = agendamentos.get(id);

        if (tarefa == null) {
            logger.warn("[AgendamentoService.cancelarTarefa] Nenhuma tarefa encontrada com o ID: {}", id);
            return;
        }

        if (tarefa.isCancelled() || tarefa.isDone()) {
            logger.info("[AgendamentoService.cancelarTarefa] Tarefa com ID: {} já está cancelada ou concluída", id);
            return;
        }

        tarefa.cancel(true);
        agendamentos.remove(id);
        logger.info("[AgendamentoService.cancelarTarefa] Tarefa com ID: {} cancelada com sucesso", id);
    }
}
