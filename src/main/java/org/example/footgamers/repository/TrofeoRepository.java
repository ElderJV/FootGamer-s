package org.example.footgamers.repository;

import org.example.footgamers.entities.Trofeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrofeoRepository extends JpaRepository<Trofeo, Long> {
}