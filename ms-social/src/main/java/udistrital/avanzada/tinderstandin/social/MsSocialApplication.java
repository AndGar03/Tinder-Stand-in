package udistrital.avanzada.tinderstandin.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Clase principal del microservicio de interacción social.
 * 
 * @author AndGar03
 */
@SpringBootApplication
public class MsSocialApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSocialApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
