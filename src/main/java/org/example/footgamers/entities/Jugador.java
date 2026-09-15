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
public class Jugador {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String contrasena;

    @Column(name = "equipo_favorito")
    private String equipoFavorito;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @OneToMany()
    private List<ParticipacionPartido> participaciones;

    @ManyToMany
    private List<Trofeo> trofeos;

    @OneToMany
    private List<Torneo> torneos;


}
