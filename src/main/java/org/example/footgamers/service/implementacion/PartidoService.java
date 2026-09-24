package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.PartidoRequestDto;
import org.example.footgamers.dto.response.PartidoResponseDto;
import org.example.footgamers.entities.Bando;
import org.example.footgamers.entities.enums.EstadoConfirmacion;
import org.example.footgamers.entities.ParticipacionPartido;
import org.example.footgamers.entities.Partido;
import org.example.footgamers.entities.Torneo;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.BandoRepository;
import org.example.footgamers.repository.PartidoRepository;
import org.example.footgamers.repository.TorneoRepository;
import org.example.footgamers.service.reglas.IPartido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidoService implements IPartido {

    private final PartidoRepository partidoRepository;
    private final TorneoRepository torneoRepository;
    private final BandoRepository bandoRepository;

    @Override
    public PartidoResponseDto crear(PartidoRequestDto request) {
        Partido partido = new Partido();
        partido.setTorneo(buscarTorneoOpcional(request.torneoId()));
        partido.setTipoPartido(request.tipoPartido());
        partido.setFecha(request.fecha());
        partido.setEstadoConfirmacion(EstadoConfirmacion.NO_CONFIRMADO);
        return toResponse(partidoRepository.save(partido));
    }

    @Override
    public Page<PartidoResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return partidoRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    public PartidoResponseDto obtenerPorId(Long id) {
        return toResponse(buscarPartido(id));
    }

    @Override
    public PartidoResponseDto actualizar(Long id, PartidoRequestDto request) {
        Partido partido = buscarPartido(id);
        partido.setTorneo(buscarTorneoOpcional(request.torneoId()));
        partido.setTipoPartido(request.tipoPartido());
        partido.setFecha(request.fecha());
        return toResponse(partidoRepository.save(partido));
    }

    @Override
    public PartidoResponseDto asignarGanadorBando(Long id, Long bandoId) {
        Partido partido = buscarPartido(id);
        validarParticipacionesConfirmadas(partido);
        partido.setGanador(buscarBando(bandoId));
        return toResponse(partidoRepository.save(partido));
    }

    @Override
    public PartidoResponseDto confirmarPartido(Long id, String resultado) {
        Partido partido = buscarPartido(id);
        partido.setResultado(resultado);
        partido.setEstadoConfirmacion(EstadoConfirmacion.CONFIRMADO);
        return toResponse(partidoRepository.save(partido));
    }

    @Override
    public void eliminar(Long id) {
        partidoRepository.delete(buscarPartido(id));
    }

    private void validarParticipacionesConfirmadas(Partido partido) {
        List<ParticipacionPartido> participaciones = partido.getParticipacionPartidos();
        boolean todasConfirmadas = participaciones != null && !participaciones.isEmpty()
                && participaciones.stream()
                .allMatch(p -> p.getEstadoConfirmacion() == EstadoConfirmacion.CONFIRMADO);
        if (!todasConfirmadas) {
            throw ApiException.solicitudInvalida(MensajeException.PARTICIPACIONES_NO_CONFIRMADAS);
        }
    }

    private Partido buscarPartido(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "partido", id)));
    }

    private Torneo buscarTorneoOpcional(Long id) {
        if (id == null) {
            return null;
        }
        return torneoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "torneo", id)));
    }

    private Bando buscarBando(Long id) {
        return bandoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "bando", id)));
    }

    private PartidoResponseDto toResponse(Partido partido) {
        Torneo torneo = partido.getTorneo();
        Bando ganador = partido.getGanador();
        return new PartidoResponseDto(
                partido.getId(),
                partido.getResultado(),
                ganador != null ? ganador.getId() : null,
                torneo != null ? torneo.getId() : null,
                partido.getTipoPartido(),
                partido.getEstadoConfirmacion(),
                partido.getFecha()
        );
    }
}