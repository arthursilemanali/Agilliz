package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IBoletoRepository extends JpaRepository<Boleto, UUID> {
    @Query("""
            SELECT b FROM Boleto b
            WHERE b.dateCreated BETWEEN :start
            AND :end
            """)
    List<Boleto> getSLABoletos(@Param("start") LocalDate start,
                               @Param("end") LocalDate end
    );

    @Query("""
            SELECT b FROM Boleto b
            WHERE b.dateCreated BETWEEN :start
            AND :end
            AND b.status != 2
            """)
    List<Boleto> getBoletosEmAndamento(@Param("start") LocalDate start,
                               @Param("end") LocalDate end
    );
}
