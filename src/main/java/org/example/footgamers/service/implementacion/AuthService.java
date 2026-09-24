package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.LoginRequestDto;
import org.example.footgamers.dto.response.LoginResponseDto;
import org.example.footgamers.entities.Jugador;
import org.example.footgamers.entities.Usuario;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.JugadorRepository;
import org.example.footgamers.repository.UsuarioRepository;
import org.example.footgamers.security.JwtService;
import org.example.footgamers.service.reglas.IAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuth {

    private final UsuarioRepository usuarioRepository;
    private final JugadorRepository jugadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.expiration-ms}")
    private long expiracionMs;

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> ApiException.noAutorizado("Credenciales inválidas"));
        if (!passwordEncoder.matches(request.contrasena(), usuario.getContrasena())) {
            throw ApiException.noAutorizado("Credenciales inválidas");
        }
        return new LoginResponseDto(
                jwtService.generarToken(usuario),
                usuario.getUsername(),
                usuario.getRol(),
                expiracionMs
        );
    }

    @Override
    public Usuario usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Usuario usuario)) {
            throw ApiException.noAutorizado("Debe iniciar sesión");
        }
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Jugador jugadorActual() {
        Usuario usuario = usuarioActual();
        return jugadorRepository.findByUsuario_Id(usuario.getId())
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "jugador", usuario.getUsername())));
    }
}