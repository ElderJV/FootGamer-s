package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.TorneoRequestDto;
import org.example.footgamers.dto.response.TorneoResponseDto;
import org.example.footgamers.entities.Categoria;
import org.example.footgamers.entities.Jugador;
import org.example.footgamers.entities.Torneo;
import org.example.footgamers.entities.Trofeo;
import org.example.footgamers.entities.enums.EstadoTorneo;
import org.example.footgamers.entities.enums.FaseTorneo;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.CategoriaRepository;
import org.example.footgamers.repository.JugadorRepository;
import org.example.footgamers.repository.TorneoRepository;
import org.example.footgamers.repository.TrofeoRepository;
import org.example.footgamers.service.reglas.ITorneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TorneoService implements ITorneo {

    private final TorneoRepository torneoRepository;
    private final CategoriaRepository categoriaRepository;
    private final JugadorRepository jugadorRepository;
    private final TrofeoRepository trofeoRepository;

    @Override
    @Transactional
    public TorneoResponseDto crear(TorneoRequestDto request) {
        Torneo torneo = new Torneo();
        torneo.setNombre(request.nombre());
        torneo.setCategoria(buscarCategoria(request.categoriaId()));
        torneo.setTrofeo(buscarTrofeo(request.trofeoId()));
        torneo.setFechaInicio(request.fechaInicio());
        torneo.setFechaFin(request.fechaFin());
        torneo.setCantidadJugadores(request.cantidadJugadores());
        torneo.setCantidadGrupos(request.cantidadGrupos());
        torneo.setFaseActual(FaseTorneo.GRUPOS);
        torneo.setEstado(EstadoTorneo.EN_GRUPOS);
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TorneoResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return torneoRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TorneoResponseDto obtenerPorId(Long id) {
        return toResponse(buscarTorneo(id));
    }

    @Override
    @Transactional
    public TorneoResponseDto actualizar(Long id, TorneoRequestDto request) {
        Torneo torneo = buscarTorneo(id);
        torneo.setNombre(request.nombre());
        torneo.setCategoria(buscarCategoria(request.categoriaId()));
        torneo.setTrofeo(buscarTrofeo(request.trofeoId()));
        torneo.setFechaInicio(request.fechaInicio());
        torneo.setFechaFin(request.fechaFin());
        torneo.setCantidadJugadores(request.cantidadJugadores());
        torneo.setCantidadGrupos(request.cantidadGrupos());
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    @Transactional
    public TorneoResponseDto asignarGanador(Long id, Long jugadorId) {
        Torneo torneo = buscarTorneo(id);
        Jugador ganador = buscarJugador(jugadorId);
        torneo.setGanador(ganador);
        torneo.setEstado(EstadoTorneo.FINALIZADO);
        torneo.setFaseActual(FaseTorneo.FINAL);
        if (torneo.getTrofeo() != null) {
            torneo.getTrofeo().setJugador(ganador);
        }
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    @Transactional
    public TorneoResponseDto inscribirJugador(Long id, Long jugadorId) {
        Torneo torneo = buscarTorneo(id);
        if (torneo.getEstado() == EstadoTorneo.FINALIZADO) {
            throw ApiException.solicitudInvalida("No se pueden inscribir jugadores en un torneo finalizado");
        }
        Jugador jugador = buscarJugador(jugadorId);
        if (torneo.getParticipantes() == null) {
            torneo.setParticipantes(new ArrayList<>());
        }
        if (torneo.getParticipantes().contains(jugador)) {
            throw ApiException.solicitudInvalida("El jugador ya está inscrito al torneo");
        }
        torneo.getParticipantes().add(jugador);
        return toResponse(torneoRepository.save(torneo));
    }

    @Override
    @Transactional
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

    private Trofeo buscarTrofeo(Long id) {
        return trofeoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "trofeo", id)));
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
                torneo.getCantidadJugadores(),
                torneo.getCantidadGrupos(),
                torneo.getFaseActual(),
                torneo.getEstado()
        );
    }
}