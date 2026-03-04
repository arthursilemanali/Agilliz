package agiliz.projetoAgiliz.repositories;

import java.util.UUID;

import agiliz.projetoAgiliz.dto.destinatario.DestinatarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import agiliz.projetoAgiliz.models.Destinatario;
import org.springframework.data.jpa.repository.Query;

public interface IDestinatarioRepository extends JpaRepository<Destinatario, UUID>{

    @Query("SELECT d FROM Destinatario d")
    Page<DestinatarioResponse> findAllResponse(Pageable pageable);
}
