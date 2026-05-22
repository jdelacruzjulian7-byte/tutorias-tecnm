package com.example.tutorias.repository;

import com.example.tutorias.model.Tutorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutoradoRepository extends JpaRepository<Tutorado, Long> {

    Optional<Tutorado> findByUsuario_Id(Long idUsuario);

    Optional<Tutorado> findByUsuario_Correo(String correo);

    Optional<Tutorado> findByMatricula(String matricula);
    
 
    List<Tutorado> findByCarrera_Id(Long idCarrera);

    /** Tutorados asignados a un tutor en un semestre dado (vía Asignacion). */
    @Query("""
        SELECT a.tutorado FROM Asignacion a
        WHERE a.tutor.id = :idTutor
          AND (:idSemestre IS NULL OR a.semestre.id = :idSemestre)
          AND a.activo = true
        """)
    List<Tutorado> findByTutorAndSemestre(@Param("idTutor") Long idTutor,
                                          @Param("idSemestre") Long idSemestre);

    /** Tutorados asignados al tutor cuyo usuario tiene este correo. */
    @Query("""
        SELECT a.tutorado FROM Asignacion a
        WHERE a.tutor.usuario.correo = :correo
          AND a.activo = true
        """)
    List<Tutorado> findByTutorCorreo(@Param("correo") String correo);
    
    List<Tutorado> findByUsuario_NombreContainingIgnoreCaseOrUsuario_ApellidoContainingIgnoreCase(
    	    String nombre, String apellido);
    	List<Tutorado> findBySemestreIngreso(String semestreIngreso);
    	
    	@Query("""
    		    SELECT t FROM Tutorado t
    		    WHERE t.id NOT IN (
    		        SELECT a.tutorado.id FROM Asignacion a
    		        WHERE a.semestre.id = :semestreId
    		        AND a.activo = true
    		    )
    		    """)
    		List<Tutorado> findTutoradosDisponibles(@Param("semestreId") Long semestreId);
}
