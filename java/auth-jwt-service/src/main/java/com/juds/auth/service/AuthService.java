package com.juds.auth.service;

import com.juds.auth.dto.AuthDtos.AuthResponse;
import com.juds.auth.dto.AuthDtos.LoginRequest;
import com.juds.auth.dto.AuthDtos.RegisterRequest;
import com.juds.auth.model.Rol;
import com.juds.auth.model.Usuario;
import com.juds.auth.repository.UsuarioRepository;
import com.juds.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Orquesta el registro y el login:
 * - register: valida que no exista, cifra la contraseña con BCrypt y guarda.
 * - login: delega la verificación de credenciales a Spring Security y, si pasa,
 *   emite un JWT.
 */
@Service
public class AuthService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public AuthResponse registrar(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) {
            throw new IllegalArgumentException("El usuario ya existe: " + req.username());
        }
        Usuario usuario = new Usuario(req.username(), encoder.encode(req.password()), Rol.USER);
        repo.save(usuario);
        return login(new LoginRequest(req.username(), req.password()));
    }

    public AuthResponse login(LoginRequest req) {
        // lanza AuthenticationException si las credenciales no cuadran
        var authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        return new AuthResponse(jwtService.generarToken(usuario));
    }
}
