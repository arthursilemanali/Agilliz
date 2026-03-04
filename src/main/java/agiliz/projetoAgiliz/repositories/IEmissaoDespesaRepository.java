package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.EmissaoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEmissaoDespesaRepository extends JpaRepository<EmissaoDespesa, UUID> {
}
