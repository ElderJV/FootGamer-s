package org.example.footgamers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.ParticipacionPartidoRequestDto;
import org.example.footgamers.dto.response.ParticipacionPartidoResponseDto;
import org.example.footgamers.service.reglas.IParticipacionPartido;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participaciones")
@RequiredArgsConstructor
public class ParticipacionPartidoController {

    private final IParticipacionPartido participacionService;

    @PostMapping
    public ResponseEntity<ParticipacionPartidoResponseDto> crear(@Valid @RequestBody ParticipacionPartidoRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participacionService.crear(request));
    }

    @GetMapping
    public ResponseEntity<Page<ParticipacionPartidoResponseDto>> obtenerTodos(
            @RequestParam(defaultValue = "0") Long pagina,
            @RequestParam(defaultValue = "10") Long tamano) {
        return ResponseEntity.ok(participacionService.obtenerTodos(pagina, tamano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipacionPartidoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(participacionService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipacionPartidoResponseDto> actualizar(@PathVariable Long id,
                                                                      @Valid @RequestBody ParticipacionPartidoRequestDto request) {
        return ResponseEntity.ok(participacionService.actualizar(id, request));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<ParticipacionPartidoResponseDto> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(participacionService.confirmar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        participacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}