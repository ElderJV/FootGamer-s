package org.example.footgamers.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    private Categoria categoria;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
    @ManyToOne
    private Jugador ganador;

    @OneToMany()
    private List<Partido> partidos;

    private long cantidadJugadores;

}
