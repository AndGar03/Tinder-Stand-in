package com.tinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal de la aplicación Tinder Clone.
 * Inicializa el contexto de Spring Boot y configura la aplicación.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TinderApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(TinderApplication.class, args);
    }
}
