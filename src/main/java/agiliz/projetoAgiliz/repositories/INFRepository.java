package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface INFRepository extends JpaRepository<NotaFiscal, UUID> {
}
