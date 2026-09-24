package org.example.footgamers.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.JugadorRequestDto;
import org.example.footgamers.dto.response.JugadorResponseDto;
import org.example.footgamers.service.reglas.IJugador;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
public class JugadorController {

    private final IJugador jugadorService;

    @PostMapping
    public ResponseEntity<JugadorResponseDto> crear(@Valid @RequestBody JugadorRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jugadorService.crear(request));
    }

    @GetMapping
    public ResponseEntity<Page<JugadorResponseDto>> obtenerTodos(
            @RequestParam(defaultValue = "0") Long pagina,
            @RequestParam(defaultValue = "10") Long tamano) {
        return ResponseEntity.ok(jugadorService.obtenerTodos(pagina, tamano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(jugadorService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponseDto> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody JugadorRequestDto request) {
        return ResponseEntity.ok(jugadorService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}