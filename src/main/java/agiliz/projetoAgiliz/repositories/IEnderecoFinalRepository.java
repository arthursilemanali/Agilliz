package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.enderecoFinal.EnderecoFinalResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.EnderecoFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IEnderecoFinalRepository extends JpaRepository<EnderecoFinal, UUID> {

    @Query("SELECT e FROM EnderecoFinal e WHERE e.colaborador = ?1")
    List<EnderecoFinalResponse> findResponseByColaborador(Colaborador colaborador);
}
