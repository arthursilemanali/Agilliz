package agiliz.projetoAgiliz.services;


import agiliz.projetoAgiliz.models.Vendedor;
import agiliz.projetoAgiliz.repositories.IVendedorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendedorService {

    private final IVendedorRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(VendedorService.class);

    public List<Vendedor> getAll(){
        return repository.findAll();
    }

    public Vendedor getPorId(UUID idUnidade) {
        logger.info("[VendedorService.getUnidadePorId] Consultando unidade com ID {}", idUnidade);

        var unidadeOpt = repository.findById(idUnidade);
        if (unidadeOpt.isEmpty()) {
            logger.error("[VendedorService.getUnidadePorId] Unidade com ID {} não encontrada", idUnidade);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unidade não encontrada");
        }

        logger.info("[VendedorService.getUnidadePorId] Unidade com ID {} encontrada", idUnidade);
        return unidadeOpt.get();
    }

    public void contabilizarRetornoTotal(Vendedor vendedor, double precoPacote) {
        logger.info("[VendedorService.contabilizarRetornoTotal] Contabilizando retorno para vendedor ID {} com preço do pacotes {}", vendedor.getIdUnidade(), precoPacote);

        vendedor.incrementarRetorno(precoPacote);
        repository.save(vendedor);

        logger.info("[VendedorService.contabilizarRetornoTotal] Retorno contabilizado para vendedor ID {}. Novo total: {}", vendedor.getIdUnidade(), vendedor.getRetornoTotal());
    }

    public LocalTime getHorarioCorteMedia() {
        logger.info("[VendedorService.getHorarioCorteMedia] Consultando horário de corte médio");

        var horaMediaSegundos = repository.findAVGHorarioCorte();
        LocalTime horaMedia = horaMediaSegundos == null
                ? LocalTime.of(12, 0)
                : LocalTime.ofSecondOfDay(horaMediaSegundos.longValue());

        logger.info("[VendedorService.getHorarioCorteMedia] Horário de corte médio: {}", horaMedia);
        return horaMedia;
    }

    public String getNomeUnidadeMaiorRetorno() {
        logger.info("[VendedorService.getNomeUnidadeMaiorRetorno] Consultando unidade com maior retorno");

        String nomeUnidade = repository.findUnidadeMaiorRetorno();
        logger.info("[VendedorService.getNomeUnidadeMaiorRetorno] Unidade com maior retorno: {}", nomeUnidade);
        return nomeUnidade;
    }

    public String getNomeUnidadeMenorRetorno() {
        logger.info("[VendedorService.getNomeUnidadeMenorRetorno] Consultando unidade com menor retorno");

        String nomeUnidade = repository.findUnidadeMenorRetorno();
        logger.info("[VendedorService.getNomeUnidadeMenorRetorno] Unidade com menor retorno: {}", nomeUnidade);
        return nomeUnidade;
    }
}
