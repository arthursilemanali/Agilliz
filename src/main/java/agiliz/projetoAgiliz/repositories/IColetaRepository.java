package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.coleta.ColetaResponse;
import agiliz.projetoAgiliz.models.Coleta;
import agiliz.projetoAgiliz.models.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IColetaRepository extends JpaRepository<Coleta, UUID> {
    @Query("SELECT c FROM Coleta c ORDER BY c.statusColeta")
    List<ColetaResponse> findAllResponse();

    @Query("""
            SELECT c FROM Coleta c WHERE c.statusColeta = 6
            """)
    List<Coleta> findAllDisponiveisConferencia();

    @Query("""
             SELECT c FROM Coleta c 
             WHERE c.coletores IS EMPTY AND c.statusColeta = 1
            """)
    List<Coleta> findAllColetasNaoAtribuidas();

    @Query("""
            SELECT c FROM Coleta c
            JOIN c.coletores col
            WHERE col.colaborador.idColaborador = :idColaborador
            AND c.statusColeta = 1
            """)
    List<Coleta> getColetasAtribuidasPorId(@Param("idColaborador") UUID idColaborador);

    @Query("""
            SELECT c
            FROM Coleta c
            JOIN c.coletores co
            WHERE co.colaborador.idColaborador IN :idColaborador
            AND c.statusColeta = 6
            """)
    List<Coleta> getColetasFinalizadasByIdColaborador(@Param("idColaborador") UUID idColaborador);

    @Query("""
             SELECT p FROM Pacote p
             WHERE p.coleta.idColeta = :idColeta
            """)
    List<Pacote> getPacotesFromColeta(UUID idColeta);

    @Query("""
             SELECT c FROM Coleta c
             WHERE c.statusColeta = 2
            """)
    List<Coleta> getColetasEmAndamento();

    @Query("""
            SELECT c FROM Coleta c
            WHERE c.diaHoraRegistro BETWEEN :start AND :end
            """)
    List<Coleta> getSlaColetas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT c FROM Coleta c WHERE c.statusColeta = 1 OR c.statusColeta = 2 ORDER BY c.statusColeta")
    List<ColetaResponse> findColetasAbertas();

    @Query("""
             SELECT c FROM Coleta c 
             JOIN c.pacotes p
             WHERE c.statusColeta = 4
             AND p.colaborador IS NULL
             AND p.tipo = 2
            """)
    List<Coleta> findAllColetasConcluidas();
}
