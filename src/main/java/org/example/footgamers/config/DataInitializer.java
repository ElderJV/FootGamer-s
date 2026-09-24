package org.example.footgamers.config;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.entities.Usuario;
import org.example.footgamers.entities.enums.Rol;
import org.example.footgamers.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.existsByUsername("imanol")) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setUsername("imanol");
        admin.setEmail("imanol@footgamers.com");
        admin.setContrasena(passwordEncoder.encode("123456"));
        admin.setRol(Rol.ADMINISTRADOR);
        usuarioRepository.save(admin);
    }
}
