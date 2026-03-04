package agiliz.projetoAgiliz.services;

import agiliz.projetoAgiliz.configs.security.Exception.ResponseEntityException;
import agiliz.projetoAgiliz.configs.security.JWT.GerenciadorTokenJWT;
import agiliz.projetoAgiliz.dto.colaborador.*;
import agiliz.projetoAgiliz.dto.dashEntregas.DashEntregas;
import agiliz.projetoAgiliz.dto.dashEntregas.MaiorEMenorEntrega;
import agiliz.projetoAgiliz.dto.dashEntregas.MesPorQtdDeEntregaDTO;
import agiliz.projetoAgiliz.dto.dashEntregas.TotalEntregaDTO;
import agiliz.projetoAgiliz.models.*;
import agiliz.projetoAgiliz.repositories.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorService {
    private static final Logger logger = LoggerFactory.getLogger(ColaboradorService.class);

    private final IColaboradorRepository repository;
    private final IPacoteRepository pacoteRepository;
    private final GerenciadorTokenJWT gerenciadorTokenJWT;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TipoColaboradorService tipoColaboradorService;
    private final IPagamentoRepository pagamentoRepository;
    private final ZonaService zonaService;
    private final ColaboradorRepositoryCustom repositoryCustom;
    private final RoleService roleService;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            "cPqikLBo7pdyw0LOuYgQYq5mRYfWlv7tsmWguN39wONXZ7GvmUZKZUwJLsRTA7GrS5JYTHvWEsFIlxNdUxZAC7oE3l8N1OIZn26SQqGx2Vi26zPmBeFgblF4WzH8GpnzmI4kGf7AD4rIl14I0mQbT3VdSakxKpSRiFV6UWPSwNjKMTazcPjhzKBhqGQqcxnyrR22Y3ePI6C3eMb4MVvznQ27eXXYrHf23cncHnPSGA"
                    .getBytes(StandardCharsets.UTF_8)
    );

    @Value("${api.url}")
    private String urlApi;

    public void atualizarToken(String token, String idColaborador) {
        var colaborador = getPorId(UUID.fromString(idColaborador));
        colaborador.setFcm_token(token);
        repository.save(colaborador);
    }

    public List<ColaboradorPacoteResponse> getColaboradoresPacote() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime startOfDay = LocalDate.now(zoneId).atStartOfDay(zoneId).toLocalDateTime();
        LocalDateTime endOfDay = LocalDate.now(zoneId).atTime(LocalTime.MAX).atZone(zoneId).toLocalDateTime();

        var colaboradores = repository.findColaboradorResponse(startOfDay, endOfDay);
        return colaboradores.stream().map(colaborador -> new ColaboradorPacoteResponse(colaborador, colaborador.getPacotes().stream().filter(pacote -> pacote.getTipo().getCodigo() == 1 && ((pacote.getStatus().getCodigo() == 6) || (pacote.getStatus().getCodigo() == 2 && isToday(pacote.getDataEntrega(), startOfDay, endOfDay)))).toList())).toList();
    }

    private boolean isToday(LocalDateTime dataEntrega, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return !dataEntrega.isBefore(startOfDay) && !dataEntrega.isAfter(endOfDay);
    }

    public ColaboradorResponse inserir(ColaboradorRequest colaboradorDTO) {
        logger.info("[ColaboradorService.inserir] Início da inserção do colaborador");
        Colaborador colaborador = new Colaborador();
        BeanUtils.copyProperties(colaboradorDTO, colaborador, "documentos");
        criptografarSenha(colaboradorDTO.senhaColaborador(), colaborador);

        if (colaboradorDTO.pagamentos() != null) {
            List<Pagamento> pagamentos = colaboradorDTO.pagamentos().stream().map(pagamentoRequest -> {
                Pagamento pagamento = new Pagamento();
                BeanUtils.copyProperties(pagamentoRequest, pagamento);
                TipoColaborador tipoColaborador = tipoColaboradorService.inserirByColaborador(pagamentoRequest.getTipoColaborador());
                pagamento.setTipoColaborador(tipoColaborador);
                pagamento.setColaborador(colaborador);
                return pagamento;
            }).collect(Collectors.toList());

            colaborador.setPagamentos(pagamentos);
        }
        Documentos documentos = new Documentos();
        if (colaboradorDTO.documentos() != null) {
            documentos.setIdColaboradorCTPS(UUID.fromString(colaboradorDTO.documentos().idColaboradorCTPS()));
            documentos.setIdColaboradorHabilitacao(UUID.fromString(colaboradorDTO.documentos().idColaboradorHabilitacao()));
            documentos.setIdColaboradorReservista(UUID.fromString(colaboradorDTO.documentos().idColaboradorReservista()));
            documentos.setIdColaboradorResidencia(UUID.fromString(colaboradorDTO.documentos().idColaboradorResicencia()));
        }
        colaborador.setDocumentos(documentos);
        ColaboradorResponse response = new ColaboradorResponse(repository.save(colaborador));
        processarPermissoes(colaborador, colaboradorDTO.roles());

        logger.info("[ColaboradorService.inserir] Colaborador inserido com sucesso: {}", colaborador);
        return response;
    }

    private void processarPermissoes(Colaborador colaborador, List<String> roles) {
        for (String role : roles) {
            switch (role) {
                case "ROLE_ADMIN" -> roleService.associarPermissao(1, colaborador);
                case "ROLE_SCANNER" -> roleService.associarPermissao(4, colaborador);
                case "ROLE_FINANCEIRO" -> roleService.associarPermissao(3, colaborador);
                case "ROLE_MOTOBOY" -> roleService.associarPermissao(2, colaborador);
                default -> throw new IllegalArgumentException("Role desconhecida: " + role);
            }
        }
    }

    private void criptografarSenha(String senha, Colaborador colaborador) {
        String senhaCriptografada = passwordEncoder.encode(senha);
        colaborador.setSenhaColaborador(senhaCriptografada);
    }

    public long countZonasAtendidas() {
        logger.info("[ColaboradorService.countZonasAtendidas] Consultando zonas atendidas");
        return zonaService.totalZonas();
    }

    public long countZonasNaoAtendidas() {
        logger.info("[ColaboradorService.countZonasAtendidas] Consultando zonas atendidas");
        return zonaService.totalZonasNaoAtendidas();
    }

    public ColaboradorResponse alterar(UUID id, ColaboradorRequestPut colaboradorRequest) {
        logger.info("[ColaboradorService.alterar] Início da alteração do colaborador com ID {}", id);
        var colaborador = getPorId(id);
        BeanUtils.copyProperties(colaboradorRequest, colaborador, "senhaColaborador");

        pagamentoRepository.deleteAllByIdColaborador(id);
        List<Pagamento> novosPagamentos = new ArrayList<>();

        for (var pagamentoRequest : colaboradorRequest.pagamentos()) {
            Pagamento pagamento = new Pagamento();
            BeanUtils.copyProperties(pagamentoRequest, pagamento);

            TipoColaborador tipoColaborador = tipoColaboradorService.inserirByColaborador(pagamentoRequest.getTipoColaborador());
            pagamento.setTipoColaborador(tipoColaborador);
            pagamento.setColaborador(colaborador);

            novosPagamentos.add(pagamento);
        }

        pagamentoRepository.saveAll(novosPagamentos);

        if (colaboradorRequest.senhaColaborador() != null && !colaboradorRequest.senhaColaborador().isEmpty())
            criptografarSenha(colaboradorRequest.senhaColaborador(), colaborador);

        ColaboradorResponse response = new ColaboradorResponse(repository.save(colaborador));
        logger.info("[ColaboradorService.alterar] Colaborador alterado com sucesso: {}", response);
        return response;
    }

    public String listarColaboradoresComMaiorEntrega(LocalDateTime start, LocalDateTime end) {
        logger.info("[ColaboradorService.listarColaboradoresComMaiorEntrega] Consultando colaborador com maior entrega");
        var colaboradoresMaisEntrega = repository.findColaboradorComMaisPacotes(start, end);
        String result = colaboradoresMaisEntrega.isEmpty() ? "-" : colaboradoresMaisEntrega.get(0);
        logger.info("[ColaboradorService.listarColaboradoresComMaiorEntrega] Colaborador com maior entrega: {}", result);
        return result;
    }

    public String listarColaboradoresComMenorEntrega(LocalDateTime start, LocalDateTime end) {
        logger.info("[ColaboradorService.listarColaboradoresComMenorEntrega] Consultando colaborador com menor entrega");
        var colaboradoresMenosEntrega = repository.findColaboradorComMenosPacotes(start, end);
        String result = colaboradoresMenosEntrega.isEmpty() ? "-" : colaboradoresMenosEntrega.get(0);
        logger.info("[ColaboradorService.listarColaboradoresComMenorEntrega] Colaborador com menor entrega: {}", result);
        return result;
    }

    public TotalEntregaDTO listarTotalEntreguesETotalPacotes(LocalDateTime start, LocalDateTime end) {
        logger.info("[ColaboradorService.listarTotalEntreguesETotalPacotes] Consultando total de entregas e pacotes");
        TotalEntregaDTO total = repository.listarTotalEntreguesETotalPacotes(start, end);
        logger.info("[ColaboradorService.listarTotalEntreguesETotalPacotes] Total de entregas e pacotes consultado com sucesso");
        return total;
    }

    public List<MesPorQtdDeEntregaDTO> listarMesPorQtdEntrega(LocalDateTime start, LocalDateTime end) {
        logger.info("[ColaboradorService.listarMesPorQtdEntrega] Consultando quantidade de entregas por mês");
        List<MesPorQtdDeEntregaDTO> result = pacoteRepository.findQtdEntregaPorMes(start, end);
        logger.info("[ColaboradorService.listarMesPorQtdEntrega] Quantidade de entregas por mês consultada com sucesso");
        return result;
    }

    public Colaborador getPorId(UUID id) {
        logger.info("[ColaboradorService.getPorId] Consultando colaborador com ID {}", id);
        Colaborador colaborador = repository.findById(id).orElseThrow(() -> {
            logger.error("[ColaboradorService.getPorId] Colaborador com ID {} não encontrado", id);
            return new ResponseEntityException(HttpStatus.NOT_FOUND, "Colaborador não encontrado", 404);
        });
        logger.info("[ColaboradorService.getPorId] Colaborador com ID {} encontrado com sucesso", id);
        return colaborador;
    }

    public List<ColaboradorResponse> findByFiltros(String campo, String valor) {
        return repositoryCustom.findByFiltro(campo, valor).stream().map(ColaboradorResponse::new).toList();
    }

    public Page<ColaboradorResponse> getColaboradoresResponsePaginados(int pagina, int tamanho) {
        logger.info("[ColaboradorService.getColaboradoresResponsePaginados] Consultando colaboradores paginados: página {}, tamanho {}", pagina, tamanho);
        var page = PageRequest.of(pagina, tamanho, Sort.by("nomeColaborador"));
        Page<ColaboradorResponse> result = repository.findAllResponsePaginado(page);
        logger.info("[ColaboradorService.getColaboradoresResponsePaginados] Total de colaboradores encontrados: {}", result.getTotalElements());
        return result;
    }

    public List<ColaboradorResponse> getColaboradoresResponse() {
        logger.info("[ColaboradorService.getColaboradoresResponse] Consultando colaboradores");
        List<ColaboradorResponse> result = repository.findAll().stream().map(ColaboradorResponse::new).toList();
        logger.info("[ColaboradorService.getColaboradoresResponse] Total de colaboradores encontrados");
        return result;
    }

    public List<Colaborador> getColaboradores() {
        logger.info("[ColaboradorService.getColaboradores] Consultando todos os colaboradores");
        List<Colaborador> colaboradores = repository.findAll();
        logger.info("[ColaboradorService.getColaboradores] Total de colaboradores encontrados: {}", colaboradores.size());
        return colaboradores;
    }

    public UsuarioLoginDTO login(UsuarioLoginDTO usuarioLoginDTO) {
        logger.info("[ColaboradorService.login] Início do login para o usuário com e-mail {}", usuarioLoginDTO.getEmail());
        try {
            final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getSenha());
            final Authentication authentication = this.authenticationManager.authenticate(credentials);
            var userFound = repository.findyEmailColaborador(usuarioLoginDTO.getEmail());
            if (!userFound.isPresent()) {
                logger.error("[ColaboradorService.login] Usuário com e-mail {} não encontrado", usuarioLoginDTO.getEmail());
                throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Usuário não encontrado", 404);
            }

            UsuarioLoginDTO user = new UsuarioLoginDTO();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = gerenciadorTokenJWT.generateToken(userFound.get().getEmailColaborador(), userFound.get().getRoles().stream().map(Role::getNome).toList());
            user.setEmail(userFound.get().getEmailColaborador());
            user.setSenha(null);
            user.setIdUsuario(userFound.get().getIdColaborador().toString());
            user.setPossuiPermissaoScanner(userFound.get().getPossuiPermissaoScanner());
            user.setToken(token);
            user.setFcm_token(userFound.get().getFcm_token());
            user.setRoles(userFound.get().getRoles().stream().map(Role::getNome).toList());
            user.setNome(userFound.get().getNomeColaborador());

            logger.info("[ColaboradorService.login] Login bem-sucedido para o usuário com e-mail {}", usuarioLoginDTO.getEmail());
            return user;
        } catch (Exception e) {
            logger.error("[ColaboradorService.login] Erro durante o login para o usuário com e-mail {}: {}", usuarioLoginDTO.getEmail(), e.getMessage());
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST, e.getMessage(), 400);
        }
    }

    public UsuarioLoginDTO loginNonAdmin(UsuarioLoginDTO usuarioLoginDTO) {
        logger.info("[ColaboradorService.login] Início do login para o usuário com e-mail {}", usuarioLoginDTO.getEmail());
        try {
            final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getSenha());
            final Authentication authentication = this.authenticationManager.authenticate(credentials);
            var userFound = repository.findyEmailColaborador(usuarioLoginDTO.getEmail());

            if (!userFound.isPresent() || !userFound.get().getPossuiPermissaoScanner()) {
                logger.error("[ColaboradorService.login] Usuário com e-mail {} não encontrado Ou não possui permissao", usuarioLoginDTO.getEmail());
                throw new ResponseEntityException(HttpStatus.NOT_FOUND, "Usuário não encontrado ou não possui permissão de login", 404);
            }

            UsuarioLoginDTO user = new UsuarioLoginDTO();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = gerenciadorTokenJWT.generateToken(userFound.get().getEmailColaborador(), userFound.get().getRoles().stream().map(Role::getNome).toList());
            user.setEmail(userFound.get().getEmailColaborador());
            user.setSenha(null);
            user.setIdUsuario(userFound.get().getIdColaborador().toString());
            user.setPossuiPermissaoScanner(userFound.get().getPossuiPermissaoScanner());
            user.setToken(token);
            user.setFcm_token(userFound.get().getFcm_token());

            logger.info("[ColaboradorService.login] Login bem-sucedido para o usuário com e-mail {}", usuarioLoginDTO.getEmail());
            return user;
        } catch (Exception e) {
            logger.error("[ColaboradorService.login] Erro durante o login para o usuário com e-mail {}: {}", usuarioLoginDTO.getEmail(), e.getMessage());
            throw new ResponseEntityException(HttpStatus.BAD_REQUEST, e.getMessage(), 400);
        }
    }

    public void deletarPorId(UUID id) {
        logger.info("[ColaboradorService.deletarPorId] Início da exclusão do colaborador com ID {}", id);
        getPorId(id);
        repository.deleteById(id);
        logger.info("[ColaboradorService.deletarPorId] Colaborador com ID {} excluído com sucesso", id);
    }

    public DashEntregas montarDash(LocalDate startDate, LocalDate endDate) {
        logger.info("[ColaboradorService.montarDash] Início da montagem do dashboard");

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        var dadosDash = new DashEntregas();
        var maiorEMenorEntrega = new MaiorEMenorEntrega();
        var limit3 = PageRequest.of(0, 3);

        maiorEMenorEntrega.setNomeColaboradorMaiorEntrega(listarColaboradoresComMaiorEntrega(startDateTime, endDateTime));
        maiorEMenorEntrega.setNomeColaboradorMenorEntrega(listarColaboradoresComMenorEntrega(startDateTime, endDateTime));

        dadosDash.setMaiorEMenorEntregaColaborador(maiorEMenorEntrega);
        dadosDash.setMesPorQtdDeEntrega(listarMesPorQtdEntrega(startDateTime, endDateTime));
        dadosDash.setRankingEntregas(pacoteRepository.listarRankingEntregas(limit3, startDateTime, endDateTime));
        dadosDash.setTotalAusentesECanceladas(repository.lisTotalAusenteECanceladas(startDateTime, endDateTime));
        dadosDash.setTotalEntregaDTO(listarTotalEntreguesETotalPacotes(startDateTime, endDateTime));
        dadosDash.setZonasAtendidas(countZonasAtendidas());
        dadosDash.setEntregasEmRota(pacoteRepository.countPacotesParaEntrega(startDateTime, endDateTime));
        dadosDash.setEntregasEmRota(pacoteRepository.countPacotesParaEntrega(startDateTime, endDateTime));
        dadosDash.setTotalNaoAtendidas(zonaService.totalZonasNaoAtendidas());

        logger.info("[ColaboradorService.montarDash] Dashboard montado com sucesso");
        return dadosDash;
    }

    public void mandarEmailAlteracaoSenha(String destinatario) {
        try {
            logger.info("[ColaboradorService.mandarEmailAlteracaoSenha] Enviando e-mail para alteração de senha para {}", destinatario);
            if (!repository.existsByEmailColaborador(destinatario)) {
                logger.warn("[ColaboradorService.mandarEmailAlteracaoSenha] E-mail {} não encontrado", destinatario);
                return;
            }
            var funcionario = repository.findByEmailColaborador(destinatario).orElseThrow(() -> new ResponseEntityException(HttpStatus.NOT_FOUND, "Colaborador não encontrado com esse e-mail", 404));

            String link = urlApi + "funcionario?tkReq=" + funcionario.getIdColaborador();
            emailService.enviarEmail(destinatario, link);
            logger.info("[ColaboradorService.mandarEmailAlteracaoSenha] E-mail para alteração de senha enviado para {}", destinatario);
        } catch (Exception e) {
            logger.error("[ColaboradorService.mandarEmailAlteracaoSenha] Erro ao enviar e-mail para o destinatario {}", destinatario);
        }
    }

    private Key getSigningKey() {
        return secretKey;
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("authorities", List.class);
    }


    public void alterarSenha(AlterarSenhaRequest dto) {
        logger.info("[ColaboradorService.alterarSenha] Início da alteração de senha para o colaborador com ID {}", dto.idColaborador());
        var colaborador = getPorId(dto.idColaborador());
        criptografarSenha(dto.senha(), colaborador);
        repository.save(colaborador);
        logger.info("[ColaboradorService.alterarSenha] Senha alterada com sucesso para o colaborador com ID {}", dto.idColaborador());
    }

    public ColaboradorResponse getResponsePorId(UUID id) {
        logger.info("[ColaboradorService.getResponsePorId] Consultando colaborador com ID {}", id);
        ColaboradorResponse response = new ColaboradorResponse(getPorId(id));
        logger.info("[ColaboradorService.getResponsePorId] Colaborador com ID {} consultado com sucesso", id);
        return response;
    }

    public FolhaDePagamento gerarFolhaDePagamento() {
        logger.info("[ColaboradorService.gerarFolhaDePagamento] Início da geração da folha de pagamento");

        FolhaDePagamento folha = new FolhaDePagamento(repository.findColaboradoresComEmissoesPagamento());

        logger.info("[ColaboradorService.gerarFolhaDePagamento] Folha de pagamento gerada com sucesso");

        return folha;
    }

    public List<Colaborador> getColaboradoresComSaldo() {
        return repository.findColaboradoresComSaldo();
    }
}
