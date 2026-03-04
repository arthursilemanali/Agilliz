package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface IVendedorRepository extends JpaRepository<Vendedor, UUID> {

    @Query("SELECT COUNT(v) FROM Vendedor v WHERE v.email = :email AND v.id_ecommerce = :idEcommerce")
    Long countByEmailAndIdEcommerce(@Param("email") String email, @Param("idEcommerce") Long idEcommerce);

    @Query("""
                  SELECT v FROM Vendedor v WHERE v.horarioCorte = :horario
            """)
    List<Vendedor> getVendedoresByHorarioCorte(@Param("horario") LocalTime horario);

    @Query("SELECT u.nomeVendedor FROM Vendedor u ORDER BY u.retornoTotal ASC LIMIT 1")
    String findUnidadeMenorRetorno();

    @Query("""
            SELECT v FROM Vendedor v
            WHERE v.tg_token = :tg_token
            """)
    Optional<Vendedor> findVendedorByTgToken(@Param("tg_token") String tgToken);

    @Query("SELECT u.nomeVendedor FROM Vendedor u ORDER BY u.retornoTotal DESC LIMIT 1")
    String findUnidadeMaiorRetorno();

    @Query(value = "SELECT AVG(TIME_TO_SEC(horario_corte)) FROM vendedor", nativeQuery = true)
    Double findAVGHorarioCorte();

}

