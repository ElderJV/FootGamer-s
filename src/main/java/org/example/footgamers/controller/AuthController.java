package org.example.footgamers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.LoginRequestDto;
import org.example.footgamers.dto.response.LoginResponseDto;
import org.example.footgamers.dto.response.UsuarioResponseDto;
import org.example.footgamers.entities.Usuario;
import org.example.footgamers.service.reglas.IAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuth authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDto> me() {
        Usuario usuario = authService.usuarioActual();
        return ResponseEntity.ok(new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol()
        ));
    }
}