package org.example.footgamers.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bando {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private int numeroLado;

    @OneToMany
    @Column(name = "jugador_partido")
    private List<ParticipacionPartido> participacionPartidos;

}
