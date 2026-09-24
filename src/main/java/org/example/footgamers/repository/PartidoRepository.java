package org.example.footgamers.repository;

import org.example.footgamers.entities.Partido;
import org.example.footgamers.entities.enums.EstadoPartido;
import org.example.footgamers.entities.enums.FaseTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    List<Partido> findByTorneo_Id(Long torneoId);

    List<Partido> findByGrupo_Id(Long grupoId);

    List<Partido> findByTorneo_IdAndFase(Long torneoId, FaseTorneo fase);

    List<Partido> findByTorneo_IdAndEstadoOrderById(Long torneoId, EstadoPartido estado);

    List<Partido> findByFuenteUno_IdOrFuenteDos_Id(Long fuenteUnoId, Long fuenteDosId);
}