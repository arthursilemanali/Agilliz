package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.emissaoPagamento.EmissaoPagamentoResponse;
import agiliz.projetoAgiliz.models.EmissaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface IEmissaoPagamentoRepository extends JpaRepository<EmissaoPagamento, UUID> {
    @Query("SELECT e FROM EmissaoPagamento e WHERE e.statusPagamento = 2")
    List<EmissaoPagamentoResponse> findAllResponse();

    @Query("SELECT COALESCE(SUM(e.valor), 0) FROM EmissaoPagamento e WHERE e.statusPagamento = 3 AND e.data BETWEEN :start AND :end")
    Double getCustoOperacional(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
            SELECT MONTH(e.data), SUM(e.valor)
            FROM EmissaoPagamento e WHERE e.data BETWEEN :start AND :end
            GROUP BY MONTH(e.data), YEAR(e.data)
            """)
    List<Object[]> findCustoOperacionalPorMes(@Param("start") LocalDate start, @Param("end") LocalDate end);

    //Optional<EmissaoPagamento> findByColaboradorAndPagoFalse(Colaborador colaborador);
}
