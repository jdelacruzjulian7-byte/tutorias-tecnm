package com.example.tutorias.repository;

import com.example.tutorias.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByTutorado_Id(Long idTutorado);

    List<Asistencia> findBySesion_Id(Long idSesion);

    Optional<Asistencia> findByTutorado_IdAndSesion_Id(Long idTutorado, Long idSesion);
    

    /** Total de sesiones del PAT del semestre. */
    @Query("""
    	    SELECT COUNT(s) FROM Sesion s
    	    WHERE s.actividad.pat.semestre.id = :idSemestre
    	      AND (s.activo = true OR s.activo IS NULL)
    	    """)
    	long countSesionesPorSemestre(@Param("idSemestre") Long idSemestre);

    /** Asistencias presentes de un tutorado en un semestre. */
    @Query("""
        SELECT COUNT(a) FROM Asistencia a
        WHERE a.tutorado.id = :idTutorado
          AND a.sesion.actividad.pat.semestre.id = :idSemestre
          AND a.presente = true
        """)
    long countPresentesPorTutoradoYSemestre(@Param("idTutorado") Long idTutorado,
                                            @Param("idSemestre") Long idSemestre);
    
    
 // Contar asistencias de un tutorado en un semestre
    @Query("""
        SELECT COUNT(a) FROM Asistencia a
        WHERE a.tutorado.id = :tutoradoId
        AND a.presente = true
        AND a.sesion.actividad.pat.semestre.id = :semestreId
        """)
    long contarPresentes(@Param("tutoradoId") Long tutoradoId,
                         @Param("semestreId") Long semestreId);

    @Query("""
        SELECT COUNT(a) FROM Asistencia a
        WHERE a.tutorado.id = :tutoradoId
        AND a.sesion.actividad.pat.semestre.id = :semestreId
        """)
    long contarTotal(@Param("tutoradoId") Long tutoradoId,
                     @Param("semestreId") Long semestreId);
}


