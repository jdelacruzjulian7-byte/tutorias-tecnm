package com.example.tutorias.repository;

import com.example.tutorias.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

	@Query("SELECT DISTINCT a.tutor FROM Asignacion a WHERE a.semestre.id = :idSemestre")
	List<Tutor> findBySemestre(@Param("idSemestre") Long idSemestre);

	Optional<Tutor> findByUsuario_Id(Long idUsuario);

	Optional<Tutor> findByUsuario_Correo(String correo);

	
	List<Tutor> findByUsuario_NombreContainingIgnoreCaseOrUsuario_ApellidoContainingIgnoreCase(
		    String nombre, String apellido);
		List<Tutor> findByCarrera_Id(Long carreraId);

}
