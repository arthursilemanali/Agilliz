package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.despesa.DespesaRequest;
import agiliz.projetoAgiliz.dto.despesa.DespesaResponse;
import agiliz.projetoAgiliz.enums.TipoDespesa;
import agiliz.projetoAgiliz.enums.VigenciaDespesa;
import agiliz.projetoAgiliz.models.Despesa;
import agiliz.projetoAgiliz.repositories.IDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DespesaService {
    private static final Logger logger = LoggerFactory.getLogger(DespesaService.class);
    private final IDespesaRepository repository;
    private final VeiculoService veiculoService;
    private final EmissaoDespesaService emissaoDespesaService;
    private final AgendamentoService agendamentoService;

    public Double getTotalDespesasVariaveis(LocalDate start, LocalDate end){
        return repository.findAllVariaveis(start, end);
    }

    public Double getTotalDespesasFixas(LocalDate start, LocalDate end){
        return repository.findAllFixas(start, end);
    }

    public Double getTotalImposto(LocalDate start, LocalDate end){
        return repository.findAllImposto(start, end);
    }


    public List<DespesaResponse> getAllResponse() {
        logger.info("[DespesaService.getAllResponse] Consultando todas as despesas");
        List<DespesaResponse> response = repository.findAllResponse();
        logger.info("[DespesaService.getAllResponse] Todas as despesas foram consultadas com sucesso");
        return response;
    }

    public Despesa getPorId(UUID id) {
        logger.info("[DespesaService.getPorId] Consultando despesa com ID {}", id);
        Despesa despesa = repository.findById(id)
                .orElseThrow(() -> new ResponseEntityException(
                        HttpStatusCode.valueOf(404),
                        "Não foi encontrado a despesa com o id especificado",
                        404
                ));
        logger.info("[DespesaService.getPorId] Despesa com ID {} encontrada", id);
        return despesa;
    }

    public DespesaResponse getResponsePorId(UUID id) {
        logger.info("[DespesaService.getResponsePorId] Consultando despesa com ID {}", id);
        DespesaResponse response = new DespesaResponse(getPorId(id));
        logger.info("[DespesaService.getResponsePorId] Despesa com ID {} consultada com sucesso", id);
        return response;
    }

    public DespesaResponse inserir(DespesaRequest dto) {
        logger.info("[DespesaService.inserir] Início da inserção de despesa");

        var despesa = new Despesa();

        if (TipoDespesa.valueOf(dto.tipoDespesa()) == TipoDespesa.IPVA) {
            if (dto.fkVeiculo() == null) {
                logger.error("[DespesaService.inserir] Despesas do tipo IPVA devem ter um veículo atrelado. Veículo não fornecido.");
                throw new ResponseEntityException(
                        HttpStatusCode.valueOf(400),
                        "Despesas do tipo IPVA devem ter um veículo atrelado",
                        400
                );
            }

            despesa.setVeiculo(veiculoService.getPorId(UUID.fromString(dto.fkVeiculo())));
        }

        BeanUtils.copyProperties(dto, despesa);
        DespesaResponse response = new DespesaResponse(repository.save(despesa));

        logger.info("[DespesaService.inserir] Despesa inserida com sucesso");

        emissaoDespesaService.cadastrarEmissao(despesa);
        if(despesa.getVigencia() != VigenciaDespesa.UNICO) registrarAgendamento(despesa);

        return response;
    }

    public DespesaResponse alterar(UUID id, DespesaRequest dto) {
        logger.info("[DespesaService.alterar] Início da alteração da despesa com ID {}", id);

        var despesa = getPorId(id);
        BeanUtils.copyProperties(dto, despesa);

        if (TipoDespesa.valueOf(dto.tipoDespesa()) == TipoDespesa.IPVA) {
            if (dto.fkVeiculo() == null) {
                logger.error("[DespesaService.alterar] Despesas do tipo IPVA devem ter um veículo atrelado. Veículo não fornecido.");
                throw new ResponseEntityException(
                        HttpStatusCode.valueOf(400),
                        "Despesas do tipo IPVA devem ter um veículo atrelado",
                        400
                );
            }

            despesa.setVeiculo(veiculoService.getPorId(UUID.fromString(dto.fkVeiculo())));
        } else if (despesa.getTipoDespesa() != TipoDespesa.IPVA) {
            despesa.setVeiculo(null);
        }

        DespesaResponse response = new DespesaResponse(repository.save(despesa));
        agendamentoService.cancelarTarefa(id.toString());

        if(despesa.getVigencia() != VigenciaDespesa.UNICO) registrarAgendamento(despesa);

        logger.info("[DespesaService.alterar] Despesa com ID {} alterada com sucesso", id);
        return response;
    }

    public void deletarPorId(UUID id) {
        logger.info("[DespesaService.deletarPorId] Início da exclusão da despesa com ID {}", id);

        getPorId(id);
        agendamentoService.cancelarTarefa(id.toString());
        repository.deleteById(id);

        logger.info("[DespesaService.deletarPorId] Despesa com ID {} excluída com sucesso", id);
    }

    private void registrarAgendamento(Despesa despesa) {
        logger.info("[DespesaService.registrarAgendamento] Registrando agendamento para a despesa com ID: {}", despesa.getIdDespesa());

        Runnable tarefa = () -> {
            logger.info("[DespesaService.registrarAgendamento] Executando tarefa agendada para despesa com ID: {}", despesa.getIdDespesa());
            emissaoDespesaService.cadastrarEmissao(despesa);
        };

        if (despesa.getVigencia() == VigenciaDespesa.MENSAL && despesa.getDiaVigencia() > 27) {
            tarefa = () -> {
                var data = LocalDate.now();
                var ultimoDiaMes = data.withDayOfMonth(data.getMonth().length(data.isLeapYear()));

                if(despesa.getDiaVigencia() > ultimoDiaMes.getDayOfMonth() && !data.isEqual(ultimoDiaMes)) return;

                logger.info("[DespesaService.registrarAgendamento] Executando tarefa agendada no último dia do mês para despesa com ID: {}", despesa.getIdDespesa());
                emissaoDespesaService.cadastrarEmissao(despesa);
            };
        }

        agendamentoService.agendarTarefa(despesa.getIdDespesa().toString(), tarefa, despesa.getCronExp());

        logger.info("[DespesaService.registrarAgendamento] Agendamento registrado com sucesso para a despesa com ID: {}", despesa.getIdDespesa());
    }
}