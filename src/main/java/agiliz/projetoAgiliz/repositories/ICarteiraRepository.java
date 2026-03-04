package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.dto.carteira.CarteiraResponse;
import agiliz.projetoAgiliz.models.Carteira;
import agiliz.projetoAgiliz.models.Colaborador;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ICarteiraRepository extends JpaRepository<Carteira, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE Carteira c SET c.saldo = c.saldo - :valor WHERE c.colaborador = :idColaborador")
    void descontarSaldo(@Param("valor") Double valor, @Param("idColaborador")Colaborador colaborador);

    @Query("SELECT d FROM Carteira d")
    List<CarteiraResponse> findAllResponse();

    @Query("""
            SELECT
                new Carteira(
                    c.idCarteira,
                    c.numeroConta,
                    c.chavePix,
                    c.agencia,
                    c.saldo
                )
            FROM Carteira c WHERE c.colaborador = ?1
            """)
    Optional<Carteira> findByColaborador(Colaborador colaborador);

    @Query("SELECT c.saldo FROM Carteira c WHERE c.colaborador = ?1")
    Optional<Double> findSaldoCarteira(Colaborador colaborador);

    @Modifying
    @Transactional
    @Query("UPDATE Carteira c SET c.saldo = 0 WHERE c.idCarteira IN ?1")
    void zerarSaldos(Set<UUID> ids);
}
