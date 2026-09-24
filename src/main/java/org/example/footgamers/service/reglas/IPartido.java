package org.example.footgamers.service.reglas;

import org.example.footgamers.dto.request.PartidoRequestDto;
import org.example.footgamers.dto.response.PartidoResponseDto;
import org.springframework.data.domain.Page;

public interface IPartido {

    PartidoResponseDto crear(PartidoRequestDto request);

    Page<PartidoResponseDto> obtenerTodos(Long pagina, Long tamano);

    PartidoResponseDto obtenerPorId(Long id);

    PartidoResponseDto actualizar(Long id, PartidoRequestDto request);

    PartidoResponseDto asignarGanadorBando(Long id, Long bandoId);

    PartidoResponseDto confirmarPartido(Long id, String resultado);

    void eliminar(Long id);
}