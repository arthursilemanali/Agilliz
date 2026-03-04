package agiliz.projetoAgiliz.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "vendedor")
@Data
public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUnidade;

    @JsonIgnore
    @OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY)
    private List<Pacote> pacotes;

    @JsonInclude
    @OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PrecificacaoZona> zonas;

    @OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY)
    private List<PagamentoAsaas> pagamentos;

    private String nomeVendedor;
    private String bairro;
    private String cidade;
    private String estado;
    private String nomeFantasia;
    private String cnpj;
    private String rua;
    private String cep;
    private Integer numero;
    private String telefoneVendedor;
    private Double retornoTotal;
    private LocalTime horarioCorte;
    private String acess_token;
    private String tg_token;
    private Long id_ecommerce;
    private String email;

    public void incrementarRetorno(double precoPacote) {
        retornoTotal += precoPacote;
    }
}
