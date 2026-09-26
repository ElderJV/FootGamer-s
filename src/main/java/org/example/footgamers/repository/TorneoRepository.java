package org.example.footgamers.repository;

import org.example.footgamers.entities.Torneo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    List<Torneo> findByGanador_Id(Long ganador);

    Page<Torneo> findByParticipantes_Id(Long jugadorId, Pageable pageable);
}
