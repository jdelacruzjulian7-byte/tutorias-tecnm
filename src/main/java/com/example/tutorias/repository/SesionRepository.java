package com.example.tutorias.repository;

import com.example.tutorias.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
	List<Sesion> findByTutor_IdOrderByFechaAsc(Long tutorId);
	List<Sesion> findByActividad_Id(Long actividadId);
}
