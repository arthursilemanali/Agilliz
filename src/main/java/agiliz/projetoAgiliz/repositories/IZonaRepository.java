package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.zona.ZonaResponsePut;
import agiliz.projetoAgiliz.models.Zona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IZonaRepository extends JpaRepository<Zona,UUID> {
    @Query("SELECT z FROM Zona z WHERE z.limiteInferiorCEP <= ?1 AND z.limiteSuperiorCEP >= ?1")
    Optional<Zona> findByCep(Integer cep);

    @Query("SELECT z FROM Zona z")
    Page<ZonaResponsePut> findAllResponsePaginado(Pageable pageable);

    @Query(
            "SELECT z.nomeZona " +
                    "FROM Zona z " +
                    "JOIN z.pacotes p " +
                    "WHERE p.status = 2 AND p.dataEntrega BETWEEN :start AND :end " +
                    "GROUP BY z.nomeZona " +
                    "ORDER BY COUNT(p) DESC"
    )
    List<String> findZonaMaiorEntrega(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("""
            SELECT COUNT(z) FROM Zona z LEFT JOIN z.pacotes p WHERE p IS NULL
            """)
    Integer countNaoAtendidas();

    @Query(
            "SELECT z.nomeZona " +
                    "FROM Zona z " +
                    "JOIN z.pacotes p " +
                    "WHERE p.status = 2 AND p.dataEntrega BETWEEN :start AND :end " +
                    "GROUP BY z.nomeZona " +
                    "ORDER BY COUNT(p) ASC"
    )
    List<String> findZonaMenorEntrega(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}
