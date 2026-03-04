package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.PacotePorcentagemDTO;
import agiliz.projetoAgiliz.dto.dashColetas.ColetasPorTempo;
import agiliz.projetoAgiliz.dto.dashColetas.ZonaRanking;
import agiliz.projetoAgiliz.dto.dashEntregas.MesPorQtdDeEntregaDTO;
import agiliz.projetoAgiliz.dto.dashEntregas.RankingEntregasDTO;
import agiliz.projetoAgiliz.dto.pacote.ColetasResponse;
import agiliz.projetoAgiliz.dto.pacote.PacoteResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Pacote;
import agiliz.projetoAgiliz.models.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IPacoteRepository extends JpaRepository<Pacote, UUID> {

    @Query("""
            SELECT p FROM Pacote p 
            WHERE p.status = 6
            AND p.colaborador.idColaborador = :idColaborador
            """)
    List<Pacote> getPacotesParaEntregarPorIdColaborador(@Param("idColaborador") UUID idColaborador);

    @Query("""
            SELECT p FROM Pacote p 
            WHERE status = 6
            AND p.colaborador IS NULL
            """)
    List<Pacote> findPacotesEmEspera();

    @Query("""
            SELECT p FROM Pacote p
            WHERE p.idEcommerce IN :ids
            """)
    List<Pacote> findAllByIdEcommerce(@Param("ids") List<Integer> idEcommerce);

    @Query("""
            SELECT p FROM Pacote p 
            WHERE p.dataEntrega BETWEEN :endOfWeek 
            AND :startOfWeek
            AND p.colaborador = :idColaborador
            AND p.status = 2
            AND tipo = 2
            """)
    List<Pacote> findAllPacotesEntreguesEssaSemana(@Param("startOfWeek") LocalDateTime startOfWeek,
                                                   @Param("endOfWeek") LocalDateTime endOfWeek,
                                                   @Param("idColaborador") Colaborador idColaborador);

    @Query("""
            SELECT p FROM Pacote p WHERE p.dataEntrega BETWEEN :endOfWeek 
            AND :startOfWeek
            AND p.colaborador = :idColaborador
            """)
    List<Pacote> findAllPacotesParaEntrega(@Param("startOfWeek") LocalDateTime startOfWeek,
                                           @Param("endOfWeek") LocalDateTime endOfWeek,
                                           @Param("idColaborador") Colaborador id);

    @Query("""
            SELECT COUNT(p) FROM Pacote p WHERE p.dataEntrega BETWEEN :endOfWeek 
            AND :startOfWeek
            AND p.colaborador = :idColaborador
            AND p.status = 1
            """)
    Integer findAllPacotesFaltaEntrega(@Param("startOfWeek") LocalDateTime startOfWeek,
                                       @Param("endOfWeek") LocalDateTime endOfWeek,
                                       @Param("idColaborador") Colaborador id);

    @Query("""
            SELECT p FROM Pacote p 
            WHERE p.status = 2 
            AND DATE(p.dataEntrega) = DATE(CURRENT_DATE) 
            AND p.colaborador = :idColaborador
            """)
    List<Pacote> findAllPacotesEntreguesHoje(@Param("idColaborador") Colaborador idColaborador);


    @Query(
            "SELECT new agiliz.projetoAgiliz.dto.dashEntregas.RankingEntregasDTO(" +
                    "z.nomeZona, " +
                    "ROUND(COUNT(p) * 100.0 / (SELECT COUNT(p2) FROM Pacote p2), 2)) " +
                    "FROM Pacote p " +
                    "JOIN p.zona z " +
                    "WHERE p.dataEntrega BETWEEN :start AND :end " +
                    "GROUP BY z.nomeZona " +
                    "ORDER BY COUNT(p) DESC"
    )
    List<RankingEntregasDTO> listarRankingEntregas(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT COUNT(p)
            FROM Pacote p 
            WHERE p.status = 1
            AND p.dataColeta BETWEEN :start AND :end
            """)
    Integer countPacotesParaEntrega(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new agiliz.projetoAgiliz.dto.PacotePorcentagemDTO(" +
            "(SUM(CASE WHEN p.status = 3 THEN 1 ELSE 0 END) * 100.0 / COUNT(p.id)), " +
            "(SUM(CASE WHEN p.status = 2 THEN 1 ELSE 0 END) * 100.0 / COUNT(p.id)), " +
            "(SUM(CASE WHEN p.status = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(p.id)) " +
            ") " +
            "FROM Pacote p WHERE p.dataEntrega BETWEEN :start AND :end")
    List<PacotePorcentagemDTO> listarPorcentagem();

    @Query("""
            SELECT p FROM Pacote p
            WHERE p.colaborador = ?1 AND
                p.status = 2 AND
                p.origem = ?2 AND
                statusPagamento = 2
            """)
    List<Pacote> findPackagesForPayment(Colaborador colaborador, int origem);

    @Query("""
            SELECT COALESCE(z.preco, 0) FROM Pacote p
            JOIN p.vendedor v
            JOIN v.zonas z
            WHERE p.status = 2 AND p.tipo = 1 AND z.tipoZona = p.zona.tipoZona
            """)
    List<Double> findZonaPrices();

    @Query("""
             SELECT
                 new agiliz.projetoAgiliz.dto.dashColetas.ZonaRanking(
                     p.zona.nomeZona, 
                     (CAST((COUNT(p) / :quantidadePacotes) AS DOUBLE)) * 100
                 )
             FROM Pacote p
             WHERE p.tipo = 2 
             AND p.dataColeta BETWEEN :start AND :end
             GROUP BY p.zona
             ORDER BY COUNT(p) DESC
            """)
    List<ZonaRanking> findZonasRanking(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end,
                                       @Param("quantidadePacotes") int quantidadePacotes);


    default List<ZonaRanking> findTop3ZonasRanking(LocalDateTime start, LocalDateTime end, int quantidadePacotes) {
        List<ZonaRanking> allZonasRanking = findZonasRanking(start, end, quantidadePacotes);
        return allZonasRanking.stream().limit(3).toList();
    }

    @Query("""
            SELECT p FROM Pacote p
            WHERE p.dataEntrega
            BETWEEN :start AND :end
            """)
    List<Pacote> findPacotesEntreguesBetween(@Param("start") LocalDateTime start
                                            , @Param("end") LocalDateTime end);

    @Query("""
                SELECT SUM(pz.preco) 
                FROM Pacote p
                JOIN p.vendedor v 
                JOIN PrecificacaoZona pz ON pz.vendedor.idUnidade = v.idUnidade 
                WHERE p.dataEntrega BETWEEN :start AND :end
            """)
    Double calcularValorTotalPacotes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new Pacote(p.status) FROM Pacote p WHERE p.tipo = 2 AND p.dataColeta BETWEEN :start AND :end")
    List<Pacote> findAllPacoteStatusOnly(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new agiliz.projetoAgiliz.dto.dashEntregas.MesPorQtdDeEntregaDTO(FUNCTION('MONTH', p.dataEntrega), COUNT(p.idPacote)) " +
            "FROM Pacote p WHERE p.dataEntrega BETWEEN :start AND :end " +
            "GROUP BY FUNCTION('MONTH', p.dataEntrega) " +
            "ORDER BY FUNCTION('MONTH', p.dataEntrega)")
    List<MesPorQtdDeEntregaDTO> findQtdEntregaPorMes(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
             SELECT
                 new agiliz.projetoAgiliz.dto.dashColetas.ColetasPorTempo(COUNT(p), p.dataColeta)
             FROM Pacote p
             WHERE p.dataColeta IS NOT NULL
             AND p.dataColeta BETWEEN :start AND :end
             GROUP BY p.dataColeta
            """)
    List<ColetasPorTempo> findColetasPorTempo(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT count(p)
            FROM Pacote p
            WHERE p.dataColeta IS NOT NULL
            AND p.dataColeta BETWEEN :start AND :end
            AND p.status = 4
            """)
    Integer countDevolvidas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT p.vendedor.nomeVendedor FROM Pacote p WHERE p.dataColeta BETWEEN :start AND :end GROUP BY p.vendedor.nomeVendedor ORDER BY COUNT(p) ASC")
    List<String> findClienteMenorColeta(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p.vendedor.nomeVendedor FROM Pacote p WHERE p.dataColeta BETWEEN :start AND :end GROUP BY p.vendedor.nomeVendedor ORDER BY COUNT(p) DESC")
    List<String> findClienteMaiorColeta(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p.vendedor FROM Pacote p WHERE p.status = 2 AND p.tipo = 2 AND p.dataColeta BETWEEN :start AND :end GROUP BY p.vendedor")
    List<Vendedor> countColetasRealizadas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p.vendedor FROM Pacote p WHERE p.status = 3 AND p.tipo = 2 AND p.dataColeta BETWEEN :start AND :end GROUP BY p.vendedor")
    List<Vendedor> countColetasCanceladas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Pacote p")
    Page<PacoteResponse> findPacotesResponse(Pageable pageable);

    @Query("SELECT p FROM Pacote p")
    Page<ColetasResponse> findColetasResponse(Pageable pageable);

    @Query("SELECT p FROM Pacote p WHERE p.tipo = 2")
    Page<PacoteResponse> findAllPacoteColetas(Pageable page);
}
