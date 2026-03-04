package agiliz.projetoAgiliz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ComprovanteEntrega {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private UUID idComprovante;

    @ManyToOne
    @JoinColumn(name = "fk_pacote")
    private Pacote pacote;

    @CreationTimestamp
    private LocalDateTime dataHoraEmissao;

    @GeneratedValue(strategy =  GenerationType.AUTO)
    private UUID anexoImagem;
}
