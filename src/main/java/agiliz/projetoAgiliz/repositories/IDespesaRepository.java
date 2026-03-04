package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.despesa.DespesaResponse;
import agiliz.projetoAgiliz.models.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IDespesaRepository extends JpaRepository<Despesa, UUID> {

    @Query("""
            SELECT SUM (d.valorDespesa) FROM Despesa d
            WHERE d.vigencia = 4 AND d.dataCadastrado
            BETWEEN :start AND :end
            """)
    Double findAllVariaveis(@Param("start") LocalDate start,
                            @Param("end") LocalDate end);

    @Query("""
            SELECT SUM (d.valorDespesa) FROM Despesa d
            WHERE d.vigencia != 4 AND d.dataCadastrado
            BETWEEN :start AND :end
            """)
    Double findAllFixas(@Param("start") LocalDate start,
                        @Param("end") LocalDate end);

    @Query("""
            SELECT SUM (d.valorDespesa) FROM Despesa d
            WHERE d.tipoDespesa = 6
            AND d.dataCadastrado
            BETWEEN :start AND :end
            """)
    Double findAllImposto(@Param("start") LocalDate start,
                        @Param("end") LocalDate end);

    @Query("SELECT d FROM Despesa d")
    List<DespesaResponse> findAllResponse();
}
