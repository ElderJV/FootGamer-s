package org.example.footgamers.repository;

import org.example.footgamers.entities.ParticipacionPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipacionPartidoRepository extends JpaRepository<ParticipacionPartido, Long> {
}