package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.veiculo.VeiculoResponse;
import agiliz.projetoAgiliz.models.Colaborador;
import agiliz.projetoAgiliz.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IVeiculoRepository extends JpaRepository<Veiculo, UUID> {

    @Query("""
            SELECT
                new Veiculo(
                   v.idVeiculo,
                   v.tipoVeiculo,
                   v.placa,
                   v.marca,
                   v.modelo,
                   new Colaborador(v.colaborador.nomeColaborador)
                )
            FROM Veiculo v
            """)
    List<VeiculoResponse> findAllResponse();

    @Query("""
            SELECT
                new Veiculo(
                   v.idVeiculo,
                   v.tipoVeiculo,
                   v.placa,
                   v.marca,
                   v.modelo
                )
            FROM Veiculo v
            WHERE v.colaborador = ?1
            """)
    List<VeiculoResponse> findVeiculosPorColaborador(Colaborador colaborador);
}
