package org.example.footgamers.repository;

import org.example.footgamers.entities.Torneo;
import org.example.footgamers.entities.enums.EstadoTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    List<Torneo> findByGanador_Id(Long ganador);
}