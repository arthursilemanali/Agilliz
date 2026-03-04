package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.manifesto.ManifestoResponse;
import agiliz.projetoAgiliz.models.Manifesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IManifestoRepository extends JpaRepository<Manifesto, UUID> {

    @Query("SELECT m FROM Manifesto m")
    List<ManifestoResponse> findAllResponse();

    Manifesto findByDataEmitidoAndTipo(LocalDate data, int tipo);
}
