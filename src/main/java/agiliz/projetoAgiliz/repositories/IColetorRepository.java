package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.coletor.ColetorRequest;
import agiliz.projetoAgiliz.dto.coletor.ColetorResponse;
import agiliz.projetoAgiliz.models.Coleta;
import agiliz.projetoAgiliz.models.Coletor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IColetorRepository extends JpaRepository<Coletor, UUID> {
    @Query("SELECT c FROM Coletor c")
    Page<ColetorResponse> findAllColetoresPaginados(Pageable pageable);

    @Query("SELECT COALESCE(SUM(c.pacotesColetados), 0) FROM Coletor c WHERE c.coleta.idColeta = ?1")
    Integer getTotalConferenciaPorColeta(UUID idColeta);

    @Query("""
            SELECT c FROM Coletor c
            WHERE c.colaborador.idColaborador = :idColaborador
            AND c.coleta.idColeta = :idColeta
            """)
    Optional<Coletor> getColetorPorIdDeFuncionarioEIdColeta(@Param("idColaborador") UUID idColaborador,
                                                            @Param("idColeta") UUID idColeta);
}
