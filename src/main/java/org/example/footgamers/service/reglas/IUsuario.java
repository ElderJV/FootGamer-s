package org.example.footgamers.service.reglas;

import org.example.footgamers.dto.request.UsuarioRequestDto;
import org.example.footgamers.dto.response.UsuarioResponseDto;
import org.springframework.data.domain.Page;

public interface IUsuario {

    UsuarioResponseDto crear(UsuarioRequestDto request);

    Page<UsuarioResponseDto> obtenerTodos(Long pagina, Long tamano);

    UsuarioResponseDto obtenerPorId(Long id);

    UsuarioResponseDto actualizar(Long id, UsuarioRequestDto request);

    void eliminar(Long id);
}