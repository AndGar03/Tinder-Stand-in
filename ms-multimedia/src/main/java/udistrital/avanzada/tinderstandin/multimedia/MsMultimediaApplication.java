package udistrital.avanzada.tinderstandin.multimedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del microservicio de Multimedia.
 * Provee capacidades CRUD para fotos de perfil.
 *
 * @author AndGar03
 */
@SpringBootApplication
public class MsMultimediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsMultimediaApplication.class, args);
    }
}
