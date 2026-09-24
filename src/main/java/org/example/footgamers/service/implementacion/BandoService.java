package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.BandoRequestDto;
import org.example.footgamers.dto.response.BandoResponseDto;
import org.example.footgamers.entities.Bando;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.BandoRepository;
import org.example.footgamers.service.reglas.IBando;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BandoService implements IBando {

    private final BandoRepository bandoRepository;

    @Override
    public BandoResponseDto crear(BandoRequestDto request) {
        Bando bando = new Bando();
        bando.setNumeroLado(request.numeroLado());
        return toResponse(bandoRepository.save(bando));
    }

    @Override
    public Page<BandoResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return bandoRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    public BandoResponseDto obtenerPorId(Long id) {
        return toResponse(buscarBando(id));
    }

    @Override
    public BandoResponseDto actualizar(Long id, BandoRequestDto request) {
        Bando bando = buscarBando(id);
        bando.setNumeroLado(request.numeroLado());
        return toResponse(bandoRepository.save(bando));
    }

    @Override
    public void eliminar(Long id) {
        bandoRepository.delete(buscarBando(id));
    }

    private Bando buscarBando(Long id) {
        return bandoRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "bando", id)));
    }

    private BandoResponseDto toResponse(Bando bando) {
        return new BandoResponseDto(
                bando.getId(),
                bando.getNumeroLado()
        );
    }
}