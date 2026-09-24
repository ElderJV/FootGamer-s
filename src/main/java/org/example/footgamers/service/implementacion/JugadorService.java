package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.JugadorRequestDto;
import org.example.footgamers.dto.response.HistorialJugadorResponseDto;
import org.example.footgamers.dto.response.HistorialPartidoResponseDto;
import org.example.footgamers.dto.response.JugadorResponseDto;
import org.example.footgamers.entities.*;
import org.example.footgamers.entities.enums.EstadoConfirmacion;
import org.example.footgamers.entities.enums.EstadoPartido;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.*;
import org.example.footgamers.service.reglas.IJugador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JugadorService implements IJugador {

    private final JugadorRepository jugadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParticipacionPartidoRepository participacionPartidoRepository;
    private final TorneoRepository torneoRepository;

    @Override
    @Transactional
    public JugadorResponseDto crear(JugadorRequestDto request) {
        Jugador jugador = new Jugador();
        jugador.setUsuario(buscarUsuario(request.usuarioId()));
        jugador.setEquipoFavorito(request.equipoFavorito());
        jugador.setFechaRegistro(LocalDate.now());
        return toResponse(jugadorRepository.save(jugador));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JugadorResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return jugadorRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public JugadorResponseDto obtenerPorId(Long id) {
        return toResponse(buscarJugador(id));
    }

    @Override
    @Transactional(readOnly = true)
    public JugadorResponseDto obtenerPorUsername(String username) {
        return toResponse(buscarJugadorPorUsername(username));
    }

    @Override
    @Transactional
    public JugadorResponseDto actualizar(Long id, JugadorRequestDto request) {
        Jugador jugador = buscarJugador(id);
        jugador.setUsuario(buscarUsuario(request.usuarioId()));
        jugador.setEquipoFavorito(request.equipoFavorito());
        return toResponse(jugadorRepository.save(jugador));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        jugadorRepository.delete(buscarJugador(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HistorialJugadorResponseDto obtenerHistorial(Long id) {
        Jugador jugador = buscarJugador(id);
        List<ParticipacionPartido> participaciones = participacionPartidoRepository
                .findByJugador_Id(jugador.getId());

        long partidosJugados = 0, victorias = 0, empates = 0, derrotas = 0;
        long golesAFavor = 0, golesEnContra = 0     ;
        List<HistorialPartidoResponseDto> partidos = new ArrayList<>();

        for (ParticipacionPartido participacion : participaciones) {
            if (participacion.getEstadoConfirmacion() != EstadoConfirmacion.CONFIRMADO) {
                continue;
            }
            Partido partido = participacion.getPartido();
            if (partido == null || partido.getEstado() != EstadoPartido.FINALIZADO) {
                continue;
            }

            int[] goles = parsearResultado(partido.getResultado());
            boolean ladoUno = participacion.getBando() != null
                    && participacion.getBando().getNumeroLado() == 1;
            int golesPropios = ladoUno ? goles[0] : goles[1];
            int golesRival = ladoUno ? goles[1] : goles[0];
            golesAFavor += golesPropios;
            golesEnContra += golesRival;

            String desenlace;
            if (golesPropios > golesRival) {
                victorias++;
                desenlace = "VICTORIA";
            } else if (golesPropios == golesRival) {
                empates++;
                desenlace = "EMPATE";
            } else {
                derrotas++;
                desenlace = "DERROTA";
            }
            partidosJugados++;

            partidos.add(new HistorialPartidoResponseDto(
                    partido.getId(),
                    partido.getTorneo() != null ? partido.getTorneo().getId() : null,
                    partido.getTorneo() != null ? partido.getTorneo().getNombre() : "Amistoso",
                    partido.getTipoPartido(),
                    partido.getFase(),
                    partido.getFecha(),
                    obtenerRival(partido, participacion),
                    partido.getResultado(),
                    golesPropios + "-" + golesRival,
                    golesRival + "-" + golesPropios,
                    desenlace,
                    participacion.getEstadoConfirmacion().name()
            ));
        }

        return new HistorialJugadorResponseDto(
                jugador.getId(),
                jugador.getUsuario().getUsername(),
                jugador.getEquipoFavorito(),
                partidosJugados,
                victorias,
                empates,
                derrotas,
                golesAFavor,
                golesEnContra,
                (long) torneoRepository.findByGanador_Id(jugador.getId()).size(),
                partidos
        );
    }

    private String obtenerRival(Partido partido, ParticipacionPartido actual) {
        return partido.getParticipacionPartidos().stream()
                .filter(p -> !p.getId().equals(actual.getId()))
                .map(p -> p.getJugador().getUsuario().getUsername())
                .findFirst()
                .orElse("Sin rival");
    }

    private int[] parsearResultado(String resultado) {
        String[] partes = resultado == null ? new String[0] : resultado.split("-");
        if (partes.length != 2) {
            return new int[]{0, 0};
        }
        try {
            return new int[]{Integer.parseInt(partes[0].trim()), Integer.parseInt(partes[1].trim())};
        } catch (NumberFormatException e) {
            return new int[]{0, 0};
        }
    }

    private Jugador buscarJugador(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "jugador", id)));
    }

    private Jugador buscarJugadorPorUsername(String username) {
        return jugadorRepository.findByUsuario_Username(username)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO_POR_USERNAME, "jugador", username)));
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