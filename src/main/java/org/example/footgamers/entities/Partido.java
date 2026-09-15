package org.example.footgamers.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.footgamers.entities.enums.EstadoConfirmacion;
import org.example.footgamers.entities.enums.TipoPartido;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resultado;


    private String ganador;

    @ManyToOne()
    private Torneo torneo;

    @Enumerated(EnumType.STRING)
    private TipoPartido tipoPartido;

    @Enumerated(EnumType.STRING)
    private EstadoConfirmacion estadoConfirmacion;

    private LocalDate fecha;

    @OneToMany
    private List<ParticipacionPartido> participacionPartidos;
}
