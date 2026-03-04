package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRole;

    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "roles")
    private Set<Colaborador> colaboradores;

    public Role(String nome) {
        this.nome = nome;
        this.colaboradores = new HashSet<>();
    }
}