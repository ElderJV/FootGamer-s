package org.example.footgamers.dto.response;

import java.time.LocalDate;

public record TorneoResponseDto(
        Long id,
        String nombre,
        Long categoriaId,
        String nombreCategoria,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Long ganadorId,
        String usernameGanador,
        Long cantidadJugadores
) {
}