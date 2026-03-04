package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.coletor.ColetorPatchRequest;
import agiliz.projetoAgiliz.dto.coletor.ColetorRequest;
import agiliz.projetoAgiliz.dto.coletor.ColetorResponse;
import agiliz.projetoAgiliz.models.Coleta;
import agiliz.projetoAgiliz.models.Coletor;
import agiliz.projetoAgiliz.repositories.IColetorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ColetorService {
    private static final Logger logger = LoggerFactory.getLogger(ColetorService.class);
    private final IColetorRepository repository;
    private final ColetaService coletaService;
    private final ColaboradorService colaboradorService;

    public ColetorResponse cadastrar(ColetorRequest dto) {
        logger.info("[ColetorService.cadastrar] Iniciando o cadastro do coletor com pacotesColetados: {}", dto.pacotesColetados());
        var coleta = validarColeta(coletaService.getPorId(dto.coleta()), dto.pacotesColetados());

        var coletor = new Coletor(dto.pacotesColetados());

        coletor.setColeta(coleta);
        coletor.setColaborador(colaboradorService.getPorId(dto.colaborador()));

        ColetorResponse response = new ColetorResponse(repository.save(coletor));
        logger.info("[ColetorService.cadastrar] Coletor cadastrado com sucesso: {}", response);
        return response;
    }

    public void salvar(Coletor coletor){
        repository.save(coletor);
    }

    public Page<ColetorResponse> getColetores(int pagina, int tamanho) {
        logger.info("[ColetorService.getColetores] Consultando coletores - Página: {}, Tamanho: {}", pagina, tamanho);
        Page<ColetorResponse> result = repository.findAllColetoresPaginados(PageRequest.of(pagina, tamanho));
        logger.info("[ColetorService.getColetores] Consulta finalizada - Total de elementos: {}", result.getTotalElements());
        return result;
    }

    public ColetorResponse getResponsePorId(UUID id) {
        logger.info("[ColetorService.getResponsePorId] Consultando coletor por ID: {}", id);
        ColetorResponse response = new ColetorResponse(getPorId(id));
        logger.info("[ColetorService.getResponsePorId] Consulta finalizada com sucesso: {}", response);
        return response;
    }

    public Coletor getPorId(UUID id) {
        logger.info("[ColetorService.getPorId] Consultando coletor com ID {}", id);
        var coletor = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[ColetorService.getPorId] Coletor com ID {} não encontrado", id);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Coletor não encontrado", 404);
                });
        logger.info("[ColetorService.getPorId] Coleta com ID {} encontrada com sucesso", id);
        return coletor;
    }

    public void alterarQuantidadePacotes(UUID id, ColetorPatchRequest dto) {
        logger.info("[ColetorService.alterarQuantidadePacotes] Alterando quantidade de pacotes do coletor com ID: {}", id);
        var coletor = getPorId(id);

        if(!dto.pacotesColetados().equals(coletor.getPacotesColetados())) {
            logger.info("[ColetorService.alterarQuantidadePacotes] Validando alteração de pacotes. Coleta atual: {}, Nova quantidade: {}",
                    coletor.getPacotesColetados(), dto.pacotesColetados());
            validarColeta(
                    coletor.getColeta(),
                    dto.pacotesColetados() - coletor.getPacotesColetados()
            );
        }

        coletor.setPacotesColetados(dto.pacotesColetados());
        repository.save(coletor);
        logger.info("[ColetorService.alterarQuantidadePacotes] Alteração de pacotes realizada com sucesso para o coletor com ID: {}", id);
    }

    public void deletarPorId(UUID id) {
        logger.info("[ColetorService.deletarPorId] Deletando coletor com ID: {}", id);
        var coleta = getPorId(id).getColeta();

        repository.deleteById(id);
        logger.info("[ColetorService.deletarPorId] Coletor com ID: {} deletado com sucesso", id);

        Integer conferencia = repository.getTotalConferenciaPorColeta(coleta.getIdColeta());
        coletaService.atualizarStatus(coleta, conferencia);
        logger.info("[ColetorService.deletarPorId] Status da coleta atualizado após exclusão do coletor com ID: {}", id);
    }

    private Coleta validarColeta(Coleta coleta, Integer pacotesColetados) {
        logger.info("[ColetorService.validarColeta] Validando coleta com ID: {} e pacotesColetados: {}", coleta.getIdColeta(), pacotesColetados);
        Integer conferencia = repository.getTotalConferenciaPorColeta(coleta.getIdColeta());

        if(pacotesColetados > coleta.getRomaneio() - conferencia) {
            logger.error("[ColetorService.validarColeta] Erro na validação: Número de pacotes excedente ao romaneio da coleta");
            throw new ResponseEntityException(
                    HttpStatus.BAD_REQUEST,
                    "Número de pacotes excedente ao romaneio da coleta",
                    400
            );
        }

        coletaService.atualizarStatus(coleta, conferencia + pacotesColetados);
        logger.info("[ColetorService.validarColeta] Coleta validada e status atualizado com sucesso");
        return coleta;
    }
}
