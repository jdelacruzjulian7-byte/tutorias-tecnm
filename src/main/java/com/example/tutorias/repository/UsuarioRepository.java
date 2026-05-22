package com.example.tutorias.repository;

import com.example.tutorias.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    
    @Query("""
    		SELECT u FROM Usuario u
    		WHERE u.rol.nombre = 'TUTORADO'
    		AND u NOT IN (SELECT td.usuario FROM Tutorado td)
    		""")
    		List<Usuario> findUsuariosDisponiblesComoTutorado();
    @Query("""
    		SELECT u FROM Usuario u
    		WHERE u.rol.nombre = 'TUTOR'
    		AND u NOT IN (SELECT t.usuario FROM Tutor t)
    		""")
    		List<Usuario> findUsuariosDisponiblesComoTutor();
    
    List<Usuario> findByRol_Nombre(String nombre);
    
    List<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
    	    String nombre, String apellido, String correo);
    

    
}