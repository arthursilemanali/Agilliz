package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Boleto;
import agiliz.projetoAgiliz.models.PagamentoAsaas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPagamentoAsaasRepository extends JpaRepository<PagamentoAsaas, UUID> {
    @Query("""
            SELECT p FROM PagamentoAsaas p
            WHERE p.id = :id
            """)
    Optional<PagamentoAsaas> findPagamentoByAsaasId(@Param("id") String id);
}
