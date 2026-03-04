package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.registroManifesto.RegistroManifestoResponse;
import agiliz.projetoAgiliz.models.Manifesto;
import agiliz.projetoAgiliz.models.RegistroManifesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRegistroManifestoRepository extends JpaRepository<RegistroManifesto, UUID> {

    @Query("""
            SELECT r FROM RegistroManifesto r
            WHERE r.manifesto = :manifesto
            """)
    List<RegistroManifesto> findByManifestoId(@Param("manifesto") Manifesto manifesto);

    @Query("SELECT r FROM RegistroManifesto r")
    Page<RegistroManifestoResponse> findAllResponsePaginados(Pageable page);
}
