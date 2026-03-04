package agiliz.projetoAgiliz.repositories;

import agiliz.projetoAgiliz.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNome(String nome);
}
