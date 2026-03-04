package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.precificacaoZona.PrecificacaoZonaResponse;
import agiliz.projetoAgiliz.models.PrecificacaoZona;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IPrecificacaoZona extends JpaRepository<PrecificacaoZona, UUID> {

    @Query("SELECT p FROM PrecificacaoZona p")
    Page<PrecificacaoZonaResponse> findAllResponsePaginado(Pageable page);

    @Query("SELECT p.preco FROM PrecificacaoZona p")
    List<Double> findZonaPrices();

    @Transactional
    @Modifying
    @Query("DELETE FROM PrecificacaoZona p WHERE p.vendedor.idUnidade = :idUnidade")
    void deleteByVendedorId(@Param("idUnidade") UUID idUnidade);
}
