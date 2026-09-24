package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.TorneoRequestDto;
import org.example.footgamers.dto.response.TorneoResponseDto;
import org.example.footgamers.entities.Categoria;
import org.example.footgamers.entities.Jugador;
import org.example.footgamers.entities.Torneo;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.CategoriaRepository;
import org.example.footgamers.repository.JugadorRepository;
import org.example.footgamers.repository.TorneoRepository;
import org.example.footgamers.service.reglas.ITorneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TorneoService implements ITorneo {

    private final TorneoRepository torneoRepository;
    private final CategoriaRepository categoriaRepository;
    private final JugadorRepository jugadorRepository;

    @Override
    public TorneoResponseDto crear(TorneoRequestDto request) {
        Torneo torneo = new Torneo();
        torneo.setNombre(request.nombre());
        torneo.setCategoria(buscarCategoria(request.categoriaId()));
        torneo.setFechaInicio(request.fechaInicio());
        torneo.setFechaFin(request.fechaFin());
        torneo.setCantidadJugadores(request.cantidadJugadores());
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    public Page<TorneoResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return torneoRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    public TorneoResponseDto obtenerPorId(Long id) {
        return toResponse(buscarTorneo(id));
    }

    @Override
    public TorneoResponseDto actualizar(Long id, TorneoRequestDto request) {
        Torneo torneo = buscarTorneo(id);
        torneo.setNombre(request.nombre());
        torneo.setCategoria(buscarCategoria(request.categoriaId()));
        torneo.setFechaInicio(request.fechaInicio());
        torneo.setFechaFin(request.fechaFin());
        torneo.setCantidadJugadores(request.cantidadJugadores());
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    public TorneoResponseDto asignarGanador(Long id, Long jugadorId) {
        Torneo torneo = buscarTorneo(id);
        torneo.setGanador(buscarJugador(jugadorId));
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    public void eliminar(Long id) {
        torneoRepository.delete(buscarTorneo(id));
    }

    private Torneo buscarTorneo(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "torneo", id)));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "categoria", id)));
    }

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "jugador", id)));
    }

    private TorneoResponseDto toResponse(Torneo torneo) {
        Jugador ganador = torneo.getGanador();
        return new TorneoResponseDto(
                torneo.getId(),
                torneo.getNombre(),
                torneo.getCategoria().getId(),
                torneo.getCategoria().getNombre(),
                torneo.getFechaInicio(),
                torneo.getFechaFin(),
                ganador != null ? ganador.getId() : null,
                ganador != null ? ganador.getUsuario().getUsername() : null,
                torneo.getCantidadJugadores()
        );
    }
}