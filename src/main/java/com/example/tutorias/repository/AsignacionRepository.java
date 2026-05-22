package com.example.tutorias.repository;

import com.example.tutorias.model.Asignacion;
import com.example.tutorias.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {

    @Query("SELECT a FROM Asignacion a WHERE a.tutorado.id = :idTutorado")
    List<Asignacion> findByTutorado(@Param("idTutorado") Long idTutorado);

    List<Asignacion> findByTutorId(Long idTutor);
    
    List<Asignacion> findByTutor_IdAndSemestre_Id(Long tutorId, Long semestreId);
    List<Asignacion> findBySemestre_Id(Long semestreId);


    @Query("""
        SELECT a.tutor FROM Asignacion a
        WHERE a.tutorado.id = :idTutorado
          AND a.semestre.id = :idSemestre
          AND a.activo = true
        """)
    Optional<Tutor> findTutorByTutoradoAndSemestre(@Param("idTutorado") Long idTutorado,
                                                   @Param("idSemestre") Long idSemestre);

    @Query("""
        SELECT COUNT(a) FROM Asignacion a
        WHERE a.tutor.id = :idTutor
          AND a.tutorado.id = :idTutorado
          AND a.semestre.id = :idSemestre
        """)
    long countDuplicado(@Param("idTutor") Long idTutor,
                        @Param("idTutorado") Long idTutorado,
                        @Param("idSemestre") Long idSemestre);

    /** Verifica si un tutorado ya está asignado a CUALQUIER tutor en un semestre */
    @Query("""
        SELECT COUNT(a) FROM Asignacion a
        WHERE a.tutorado.id = :idTutorado
          AND a.semestre.id = :idSemestre
          AND a.activo = true
        """)
    long countTutoradoEnSemestre(@Param("idTutorado") Long idTutorado,
                                 @Param("idSemestre") Long idSemestre);
}