package agiliz.projetoAgiliz.models;

import agiliz.projetoAgiliz.dto.pacote.Destination;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "destinatario")
public class Destinatario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idDestinatario;
    private String nome;
    private String rua;
    private String cep;
    private Integer numero;
    private String bairro;
    private String estado;
    private Long telefone;
    private Integer ecommerce_user_id;

    @JsonIgnore
    @OneToMany(mappedBy = "destinatario")
    private List<Pacote> pacotes;

    public Destinatario(Destination destination){
        this.nome = destination.receiver_name();
        this.rua = destination.shipping_address()==null?null:destination.shipping_address().street_name();
        this.cep = destination.shipping_address()==null?null:destination.shipping_address().zip_code();
        this.numero = destination.shipping_address()==null?null: Integer.valueOf(destination.shipping_address().street_number());
        this.bairro = destination.shipping_address().neighborhood()==null?null:destination.shipping_address().neighborhood().name();
        this.estado = destination.shipping_address().state()==null?null:destination.shipping_address().state().name();
        this.telefone = destination.shipping_address()==null?null:Long.valueOf(destination.receiver_phone());
    }
}
