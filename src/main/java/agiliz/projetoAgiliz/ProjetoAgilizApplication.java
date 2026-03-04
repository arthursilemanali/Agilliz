package agiliz.projetoAgiliz;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableFeignClients
@EnableCaching
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class
ProjetoAgilizApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoAgilizApplication.class, args);
	}

}
