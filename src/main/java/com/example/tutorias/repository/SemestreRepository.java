package com.example.tutorias.repository;

import com.example.tutorias.model.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long> {

    Optional<Semestre> findFirstByActivoTrue();

    /** Para validar duplicados: mismo periodo + año */
    Optional<Semestre> findByPeriodoAndAnio(String periodo, Integer anio);
}