package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.JugadorRequestDto;
import org.example.footgamers.dto.response.JugadorResponseDto;
import org.example.footgamers.entities.Jugador;
import org.example.footgamers.entities.Usuario;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.JugadorRepository;
import org.example.footgamers.repository.UsuarioRepository;
import org.example.footgamers.service.reglas.IJugador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JugadorService implements IJugador {

    private final JugadorRepository jugadorRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public JugadorResponseDto crear(JugadorRequestDto request) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(buscarUsuario(request.usuarioId()));
        jugador.setEquipoFavorito(request.equipoFavorito());
        jugador.setFechaRegistro(LocalDate.now());
        return toResponse(jugadorRepository.save(jugador));
    }

    @Override
    public Page<JugadorResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return jugadorRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    public JugadorResponseDto obtenerPorId(Long id) {
        return toResponse(buscarJugador(id));
    }

    @Override
    public JugadorResponseDto actualizar(Long id, JugadorRequestDto request) {
        Jugador jugador = buscarJugador(id);
        jugador.setUsuario(buscarUsuario(request.usuarioId()));
        jugador.setEquipoFavorito(request.equipoFavorito());
        return toResponse(jugadorRepository.save(jugador));
    }

    @Override
    public void eliminar(Long id) {
        jugadorRepository.delete(buscarJugador(id));
    }

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "jugador", id)));
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "usuario", id)));
    }

    private JugadorResponseDto toResponse(Jugador jugador) {
        return new JugadorResponseDto(
                jugador.getId(),
                jugador.getUsuario().getId(),
                jugador.getUsuario().getUsername(),
                jugador.getEquipoFavorito(),
                jugador.getFechaRegistro()
        );
    }
}