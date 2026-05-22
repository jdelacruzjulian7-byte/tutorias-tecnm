package com.example.tutorias.repository;

import com.example.tutorias.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByPat_IdOrderBySemanaAsc(Long patId);
    List<Actividad> findByPat_Carrera_Id(Long carreraId);
    List<Actividad> findByFecha(LocalDate fecha);
}