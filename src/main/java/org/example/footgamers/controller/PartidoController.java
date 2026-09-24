package org.example.footgamers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.PartidoRequestDto;
import org.example.footgamers.dto.response.PartidoResponseDto;
import org.example.footgamers.service.reglas.IPartido;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final IPartido partidoService;

    @PostMapping
    public ResponseEntity<PartidoResponseDto> crear(@Valid @RequestBody PartidoRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partidoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<Page<PartidoResponseDto>> obtenerTodos(
            @RequestParam(defaultValue = "0") Long pagina,
            @RequestParam(defaultValue = "10") Long tamano) {
        return ResponseEntity.ok(partidoService.obtenerTodos(pagina, tamano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(partidoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidoResponseDto> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody PartidoRequestDto request) {
        return ResponseEntity.ok(partidoService.actualizar(id, request));
    }

    @PutMapping("/{id}/ganador/{bandoId}")
    public ResponseEntity<PartidoResponseDto> asignarGanadorBando(@PathVariable Long id,
                                                                  @PathVariable Long bandoId) {
        return ResponseEntity.ok(partidoService.asignarGanadorBando(id, bandoId));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PartidoResponseDto> confirmarPartido(@PathVariable Long id,
                                                               @RequestParam String resultado) {
        return ResponseEntity.ok(partidoService.confirmarPartido(id, resultado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        partidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}