package com.example.tutorias.repository;

import com.example.tutorias.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Buscar rol por nombre (para asignar rol en Spring Security)
    Optional<Rol> findByNombre(String nombre);
}