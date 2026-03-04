package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.models.Despesa;
import agiliz.projetoAgiliz.models.EmissaoDespesa;
import agiliz.projetoAgiliz.repositories.IEmissaoDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmissaoDespesaService {

    private static final Logger logger = LoggerFactory.getLogger(EmissaoDespesaService.class);
    private final IEmissaoDespesaRepository repository;

    public void cadastrarEmissao(Despesa despesa) {
        logger.info("[EmissaoDespesaService.cadastrarEmissao] Iniciando cadastro de emissão para a despesa com ID: {}", despesa.getIdDespesa());

        repository.save(new EmissaoDespesa(despesa));

        logger.info("[EmissaoDespesaService.cadastrarEmissao] Emissão cadastrada com sucesso para a despesa com ID: {}", despesa.getIdDespesa());
    }
}
