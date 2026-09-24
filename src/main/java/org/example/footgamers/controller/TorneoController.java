package org.example.footgamers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.TorneoRequestDto;
import org.example.footgamers.dto.response.TorneoResponseDto;
import org.example.footgamers.service.reglas.ITorneo;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private final ITorneo torneoService;

    @PostMapping
    public ResponseEntity<TorneoResponseDto> crear(@Valid @RequestBody TorneoRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crear(request));
    }

    @GetMapping
    public ResponseEntity<Page<TorneoResponseDto>> obtenerTodos(
            @RequestParam(defaultValue = "0") Long pagina,
            @RequestParam(defaultValue = "10") Long tamano) {
        return ResponseEntity.ok(torneoService.obtenerTodos(pagina, tamano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TorneoResponseDto> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody TorneoRequestDto request) {
        return ResponseEntity.ok(torneoService.actualizar(id, request));
    }

    @PutMapping("/{id}/ganador/{jugadorId}")
    public ResponseEntity<TorneoResponseDto> asignarGanador(@PathVariable Long id,
                                                            @PathVariable Long jugadorId) {
        return ResponseEntity.ok(torneoService.asignarGanador(id, jugadorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        torneoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}