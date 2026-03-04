package agiliz.projetoAgiliz.configs.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue coletaQueue() {
        return new Queue("coletaQueue", true);
    }

    @Bean
    public Queue testeQueue() {
        return new Queue("testeQueue", true);
    }


    @Bean
    public Queue vendedorQueue() {
        return new Queue("vendedorQueue", true);
    }

    @Bean
    public Queue atualizarTgQueue() {
        return new Queue("atualizarTgQueue", true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("agilizExchange");
    }
}