package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.transacao.TransacaoResponse;
import agiliz.projetoAgiliz.models.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITransacaoRepository extends JpaRepository<Transacao, UUID> {

    @Query("SELECT t FROM Transacao t ORDER BY t.dataHoraTransacao DESC")
    Page<TransacaoResponse> findAllResponse(Pageable pageable);

    @Query("SELECT t FROM Transacao t WHERE t.colaborador.idColaborador = :idColaborador ORDER BY t.dataHoraTransacao DESC")
    Page<TransacaoResponse> findByColaboradorResponse(Pageable pageable, UUID idColaborador);
}
