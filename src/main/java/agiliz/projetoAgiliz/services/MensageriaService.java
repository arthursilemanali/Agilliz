package agiliz.projetoAgiliz.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MensageriaService<T> {
    private String mensagemCliente;
    private String mensagemServidor;
    private T data;
    private Integer status;

    public MensageriaService<T> mensagemCliente(String mensagemCliente) {
        setMensagemCliente(mensagemCliente);
        return this;
    }

    public MensageriaService<T> mensagemServidor(String mensagemServidor) {
        setMensagemServidor(mensagemServidor);
        return this;
    }

    public MensageriaService<T> data(T data) {
        setData(data);
        return this;
    }

    public MensageriaService<T> status(Integer status) {
        setStatus(status);
        return this;
    }

    public MensageriaService(String mensagemCliente, T data, Integer status) {
        this.mensagemCliente = mensagemCliente;
        this.data = data;
        this.status = status;
    }

    public MensageriaService(String mensagemCliente, T data) {
        this.mensagemCliente = mensagemCliente;
        this.data = data;
    }

    public MensageriaService(String mensagemCliente, Integer status) {
        this.mensagemCliente = mensagemCliente;
        this.status = status;
    }

    public MensageriaService( T data, Integer status){
        this.data = data;
        this.status = status;
    }
}
