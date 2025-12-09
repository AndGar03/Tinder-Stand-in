package udistrital.avanzada.tinderstandin.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Clase principal del microservicio de usuarios.
 * 
 * @author SanSantax
 * @author AndGar03
 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"udistrital.avanzada.tinderstandin.usuarios.entidades"})
public class MsUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUsuariosApplication.class, args);
    }
}
