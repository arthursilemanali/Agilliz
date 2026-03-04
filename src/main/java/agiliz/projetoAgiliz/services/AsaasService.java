package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoDashDTO;
import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoRequestDTO;
import agiliz.projetoAgiliz.dto.asaas.boleto.BoletoResponseDTO;
import agiliz.projetoAgiliz.dto.asaas.nf.NfRequestDTO;
import agiliz.projetoAgiliz.dto.asaas.pagamento.PagamentoResponseDTO;
import agiliz.projetoAgiliz.dto.asaas.webHook.WebhookDTO;
import agiliz.projetoAgiliz.enums.AsaasEnum;
import agiliz.projetoAgiliz.models.Boleto;
import agiliz.projetoAgiliz.models.NotaFiscal;
import agiliz.projetoAgiliz.models.PagamentoAsaas;
import agiliz.projetoAgiliz.repositories.IBoletoRepository;
import agiliz.projetoAgiliz.repositories.INFRepository;
import agiliz.projetoAgiliz.repositories.IPagamentoAsaasRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class AsaasService {

    private static final Logger logger = LoggerFactory.getLogger(AsaasService.class);

    @Autowired
    private IPagamentoAsaasRepository pagamentoRepository;

    @Autowired
    private IBoletoRepository boletoRepository;

    @Autowired
    private INFRepository nfRepository;

    @Autowired
    private VendedorService vendedorService;

    public void cadastrarBoleto(BoletoRequestDTO boletoRequest) {
        logger.info("Iniciando o cadastro de um novo boleto com o id: {}", boletoRequest.id());
        try {
            var boleto = new Boleto(boletoRequest);
            var pagamento = new PagamentoAsaas(boleto);
            var vendedor = vendedorService.getPorId(UUID.fromString(boletoRequest.idVendedor()));
            pagamento.setVendedor(vendedor);
            boletoRepository.save(boleto);
            pagamentoRepository.save(pagamento);
            logger.info("Boleto e pagamento cadastrados com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao cadastrar boleto e pagamento: {}", e.getMessage());
            throw e;
        }
    }

    public BoletoResponseDTO getBoletoPorId(String idBoleto) {
        logger.info("Buscando boleto com o id: {}", idBoleto);
        Boleto boleto = boletoRepository.findById(UUID.fromString(idBoleto))
                .orElseThrow(() -> {
                    logger.error("Boleto não encontrado para o id: {}", idBoleto);
                    return new ResponseEntityException(HttpStatus.NOT_FOUND, "Boleto não encontrado", 404);
                });

        logger.info("Boleto encontrado: {}", boleto.getIdBoleto());
        return new BoletoResponseDTO(boleto);
    }

    public void deletarBoletoPorId(String idBoleto) {
        logger.info("Iniciando a deleção do boleto com o id: {}", idBoleto);
        if (!boletoRepository.existsById(UUID.fromString(idBoleto))) {
            logger.error("Boleto não encontrado para o id: {}", idBoleto);
            throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Boleto não encontrado", 404);
        }
        boletoRepository.deleteById(UUID.fromString(idBoleto));
        logger.info("Boleto com o id: {} deletado com sucesso", idBoleto);
    }

    public List<BoletoDashDTO> getSLABoletos(LocalDate start, LocalDate end) {
        return boletoRepository.getSLABoletos(start, end).stream()
                .map(BoletoDashDTO::new).toList();
    }

    public List<BoletoDashDTO> getBoletosEmAndamento(LocalDate start, LocalDate end) {
        return boletoRepository.getSLABoletos(start, end).stream()
                .map(BoletoDashDTO::new).toList();
    }

    @Transactional
    public PagamentoResponseDTO atualizarStatusPagamento(WebhookDTO dto) {
        logger.info("Atualizando status do pagamento para o pagamento {}", dto);
        var pagamento = pagamentoRepository.findPagamentoByAsaasId(dto.payment().id()).orElseThrow(() -> {
            logger.error("Pagamento não encontrado para o id: {}", dto.payment().id());
            return new ResponseEntityException(HttpStatus.NOT_FOUND, "Pagamento não encontrado", 404);
        });

        pagamento.setStatus(AsaasEnum.fromString(dto.event()).getCodigo());
        if(pagamento.getStatus()==3){
            pagamento.setDataVencimento(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        }
        pagamento.setPaidDate(dto.payment().paidDate());
        pagamento.getBoleto().setStatus(AsaasEnum.fromString(dto.event()).getCodigo());
        pagamentoRepository.save(pagamento);

        logger.info("Status do pagamento e boleto atualizados com sucesso.");
        return new PagamentoResponseDTO(pagamento);
    }

    public void agendarNf(NfRequestDTO nfRequest) {
        var pagamento = pagamentoRepository.findPagamentoByAsaasId(nfRequest.payment())
                .orElseThrow(() -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Pagamento não encontrado", 404));

        var nf = new NotaFiscal(nfRequest);
        nf.setPagamentoAsaas(pagamento);
        nfRepository.save(nf);
    }
}