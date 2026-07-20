package com.juds.clientes.config;

import com.juds.clientes.model.Cliente;
import com.juds.clientes.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Carga un par de clientes al arrancar para que la API no esté vacía en la demo.
 * Un CommandLineRunner corre una vez, justo después del arranque.
 */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner seed(ClienteRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Cliente("Ana Torres", "ana@mail.com", true));
                repo.save(new Cliente("Luis Pérez", "luis@mail.com", false));
            }
        };
    }
}
