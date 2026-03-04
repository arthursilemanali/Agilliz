package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.OS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IOSRepository extends JpaRepository<OS, UUID> {
    @Query("""
            SELECT o FROM OS o 
            WHERE o.status = 2
            """)
    List<OS> findAllOSAguardandoPagamento();

    @Query("""
            SELECT o FROM OS o 
            WHERE o.status = 1
            """)
    List<OS> findHistorico();
}
