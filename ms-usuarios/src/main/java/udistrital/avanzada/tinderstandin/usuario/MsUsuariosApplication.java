package udistrital.avanzada.tinderstandin.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Clase principal del microservicio de usuarios.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@SpringBootApplication
@EnableFeignClients
public class MsUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsuariosApplication.class, args);
    }
}
