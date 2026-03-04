package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.setor.BonificacaoLider;
import agiliz.projetoAgiliz.dto.setor.SetorResponse;
import agiliz.projetoAgiliz.models.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISetorRepository extends JpaRepository<Setor, UUID> {
    @Query("SELECT s FROM Setor s")
    List<SetorResponse> findAllResponse();

    @Query("SELECT COUNT(s.idSetor) > 0 FROM Setor s WHERE s.liderSetor.idColaborador = ?1")
    boolean existsSetorByLider(UUID idColaborador);

    @Query("""
        SELECT s
        FROM Setor s
        WHERE
            CAST(s.limiteInferiorCep AS INTEGER) <= CAST(SUBSTRING(?1, 1, LENGTH(s.limiteInferiorCep)) AS INTEGER) AND
            CAST(SUBSTRING(?1, 1, LENGTH(s.limiteInferiorCep)) AS INTEGER) <= CAST(s.limiteSuperiorCep AS INTEGER)
    """)
    Optional<Setor> findByCepRange(String cep);


    @Query("""
        SELECT
            new agiliz.projetoAgiliz.dto.setor.BonificacaoLider(s.liderSetor, COUNT(p), s.valorBonificacao)
        FROM Setor s
        JOIN s.pacotes p
        WHERE
            s.liderSetor IS NOT NULL
            AND p.dataEntrega >= ?1
            AND p.dataEntrega <= ?2
        GROUP BY s.liderSetor
    """)
    List<BonificacaoLider> findBonificacaoVigenteLideres(LocalDateTime vigenciaInicio, LocalDateTime vigenciaFim);

    @Query("SELECT COUNT(s.idSetor) > 0 FROM Setor s WHERE s.limiteInferiorCep = ?1 OR s.limiteSuperiorCep = ?2")
    boolean existsByRangeCep(String limiteInferiorCep, String limiteSuperiorCep);
}
