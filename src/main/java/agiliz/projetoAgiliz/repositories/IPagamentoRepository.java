package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.pagamento.PagamentoResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Pagamento;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPagamentoRepository extends JpaRepository<Pagamento, UUID> {
    List<Pagamento> findByColaborador(Colaborador colaborador);

    @Query("""
            SELECT p FROM Pagamento p
            WHERE p.colaborador = :colaborador
            AND p.tipoPagamento = :tipo
            """)
    Optional<Pagamento> findByColaboradorAndTipo(@Param("colaborador") Colaborador colaborador,
                                                 @Param("tipo") int tipo);

    @Query("SELECT p FROM Pagamento p")
    Page<PagamentoResponse> findAllResponsePaginados(Pageable page);

    @Transactional
    @Modifying
    @Query("DELETE FROM Pagamento p WHERE p.colaborador.idColaborador = :idColaborador")
    void deleteAllByIdColaborador(UUID idColaborador);

    @Query("SELECT p FROM Pagamento p WHERE p.tipoPagamento = 3")
    List<Pagamento> findSalarios();
}
