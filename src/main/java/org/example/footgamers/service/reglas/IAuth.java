package org.example.footgamers.service.reglas;

import org.example.footgamers.dto.request.LoginRequestDto;
import org.example.footgamers.dto.response.LoginResponseDto;
import org.example.footgamers.entities.Jugador;
import org.example.footgamers.entities.Usuario;

public interface IAuth {

    LoginResponseDto login(LoginRequestDto request);

    Usuario usuarioActual();

    Jugador jugadorActual();
}