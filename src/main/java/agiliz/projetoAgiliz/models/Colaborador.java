package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "funcionario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Colaborador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idColaborador;
    private String nomeColaborador;
    private String cpf;
    private String rg;
    private String classeCarteira;
    private LocalDate dataNascimento;
    private String emailColaborador;
    private String senhaColaborador;
    private LocalDate dataAdmissao;
    private String telefoneColaborador;
    private String fcm_token;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<EnderecoFinal> enderecosFinais;

    @OneToMany(mappedBy = "colaborador")
    private List<Veiculo> veiculosFuncionario;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Pacote> pacotes;

    @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos;

    @OneToMany(mappedBy = "colaborador", fetch = FetchType.LAZY)
    private List<EmissaoPagamento> emissoesPagamento;

    @OneToOne(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true)
    private Documentos documentos;

    @OneToOne(mappedBy = "colaborador", cascade = CascadeType.ALL)
    private Carteira carteira;

    private Boolean possuiPermissaoScanner;

    public Colaborador(String emailColaborador, String senhaColaborador) {
        this.emailColaborador = emailColaborador;
        this.senhaColaborador = senhaColaborador;
    }

    public Colaborador(String email, String senha, UUID id, Boolean possuiPermissaoScanner) {
        this.emailColaborador = email;
        this.senhaColaborador = senha;
        this.idColaborador= id;
        this.possuiPermissaoScanner = possuiPermissaoScanner;
    }

    public Colaborador(
        UUID idColaborador,
        String nomeColaborador,
        String cpf,
        String rg
    ) {
        this.idColaborador = idColaborador;
        this.nomeColaborador = nomeColaborador;
        this.cpf = cpf;
        this.rg = rg;
    }

    public Colaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }
}
