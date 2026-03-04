package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.VigenciaPagamentoAut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IVigenciaPagamentoAutRepository  extends JpaRepository<VigenciaPagamentoAut, UUID> {
    @Query("SELECT COUNT(v.idVigenciaPagamentoAut) > 0 FROM VigenciaPagamentoAut v")
    boolean jaCadastrado();

    @Query("SELECT v FROM VigenciaPagamentoAut v")
    Optional<VigenciaPagamentoAut> findVigenciaPagamentoAut();
}
