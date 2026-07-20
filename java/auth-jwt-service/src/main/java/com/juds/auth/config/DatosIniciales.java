package com.juds.auth.config;

import com.juds.auth.model.Rol;
import com.juds.auth.model.Usuario;
import com.juds.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Crea un usuario ADMIN de arranque para poder probar el rol protegido.
 * usuario: admin  /  contraseña: admin123
 */
@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner seedAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                repo.save(new Usuario("admin", encoder.encode("admin123"), Rol.ADMIN));
            }
        };
    }
}
