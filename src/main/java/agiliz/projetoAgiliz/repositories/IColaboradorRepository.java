package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.colaborador.ColaboradorFolha;
import agiliz.projetoAgiliz.dto.colaborador.ColaboradorResponse;
import agiliz.projetoAgiliz.dto.colaborador.LoginDTO;
import agiliz.projetoAgiliz.dto.colaborador.MatrizColaboradorDTO;
import agiliz.projetoAgiliz.dto.dashEntregas.TotalAusenteECanceladasDTO;
import agiliz.projetoAgiliz.dto.dashEntregas.TotalEntregaDTO;
import agiliz.projetoAgiliz.models.Colaborador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IColaboradorRepository extends JpaRepository<Colaborador, UUID> {

    @Query("SELECT f FROM Colaborador f WHERE f.emailColaborador = :email")
    Optional<Colaborador> findyEmailColaborador(@Param("email") String email);

    @Query("SELECT new agiliz.projetoAgiliz.dto.colaborador.MatrizColaboradorDTO(e.valor, f.cpf) FROM EmissaoPagamento e LEFT JOIN e.colaborador f")
    List<MatrizColaboradorDTO> listarMatriz();

    @Query("""
            SELECT DISTINCT c FROM Colaborador c
            JOIN c.pacotes p
            WHERE (p.status = 1
            OR (p.status = 2 AND p.dataEntrega BETWEEN :startOfDay AND :endOfDay))
            AND p.tipo = 1
            """)
    List<Colaborador> findColaboradorResponse(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
                    SELECT new agiliz.projetoAgiliz.dto.dashEntregas.TotalEntregaDTO(
                    COUNT(CASE WHEN p.status = 2 THEN p.idPacote ELSE NULL END) AS entregues,
                    COUNT(p.idPacote) AS total
                    ) FROM Pacote p WHERE p.dataEntrega BETWEEN :start AND :end
            """)
    TotalEntregaDTO listarTotalEntreguesETotalPacotes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new agiliz.projetoAgiliz.dto.dashEntregas.TotalAusenteECanceladasDTO(" +
            "SUM(CASE WHEN p.status = 3 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status = 5 THEN 1 ELSE 0 END)) " +
            "FROM Pacote p WHERE p.dataColeta BETWEEN :start AND :end")
    TotalAusenteECanceladasDTO lisTotalAusenteECanceladas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT new agiliz.projetoAgiliz.dto.dashEntregas.TotalEntregaDTO(
            COUNT(CASE WHEN p.status = 2 THEN p.idPacote ELSE NULL END) AS entregues,
            COUNT(p.idPacote) AS total
            ) FROM Pacote p
            """)
    TotalEntregaDTO listarTotalEmRotaETotalPacotes();

    @Query("SELECT p.colaborador.nomeColaborador " +
            "FROM Pacote p " +
            "WHERE p.status = 2 AND p.dataEntrega BETWEEN :start AND :end " +
            "GROUP BY p.colaborador.nomeColaborador " +
            "ORDER BY COUNT(p.idPacote) DESC")
    List<String> findColaboradorComMaisPacotes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p.colaborador.nomeColaborador " +
            "FROM Pacote p " +
            "WHERE p.status = 2 AND p.dataEntrega BETWEEN :start AND :end " +
            "GROUP BY p.colaborador.nomeColaborador " +
            "ORDER BY COUNT(p.idPacote) ASC")
    List<String> findColaboradorComMenosPacotes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    boolean existsByEmailColaborador(String email);

    Optional<Colaborador> findByEmailColaborador(String email);

    @Query("SELECT c FROM Colaborador c")
    Page<ColaboradorResponse> findAllResponsePaginado(Pageable pageable);

    @Query("""
            SELECT c FROM Colaborador c
            JOIN FETCH c.emissoesPagamento e WHERE e.statusPagamento = 3
            """)
    List<ColaboradorFolha> findColaboradoresComEmissoesPagamento();

    @Query(value = "SELECT * FROM funcionario WHERE :campo = :valor", nativeQuery = true)
    List<Colaborador> findByFiltro(@Param("campo") String campo, @Param("valor") String valor);

    @Query("""
        SELECT c FROM Colaborador c JOIN c.carteira WHERE c.carteira IS NOT NULL AND c.carteira.saldo > 0
    """)
    List<Colaborador> findColaboradoresComSaldo();
}
