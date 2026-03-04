package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.membroSetor.MembroSetorResponse;
import agiliz.projetoAgiliz.models.MembroSetor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IMembroSetorRepository extends JpaRepository<MembroSetor, UUID> {
    @Query("SELECT m FROM MembroSetor m")
    List<MembroSetorResponse> findAllResponse();
}
