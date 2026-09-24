package org.example.footgamers.service.reglas;

import org.example.footgamers.dto.request.JugadorRequestDto;
import org.example.footgamers.dto.response.HistorialJugadorResponseDto;
import org.example.footgamers.dto.response.JugadorResponseDto;
import org.springframework.data.domain.Page;

public interface IJugador {

    JugadorResponseDto crear(JugadorRequestDto request);

    Page<JugadorResponseDto> obtenerTodos(Long pagina, Long tamano);

    JugadorResponseDto obtenerPorId(Long id);

    JugadorResponseDto obtenerPorUsername(String username);

    JugadorResponseDto actualizar(Long id, JugadorRequestDto request);

    void eliminar(Long id);

    HistorialJugadorResponseDto obtenerHistorial(Long id);
}