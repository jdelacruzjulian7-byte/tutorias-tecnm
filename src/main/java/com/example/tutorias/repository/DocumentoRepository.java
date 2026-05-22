package com.example.tutorias.repository;

import com.example.tutorias.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByUsuario_Id(Long idUsuario);

    List<Documento> findByUsuario_Correo(String correo);

    List<Documento> findByTipo(String tipo);
}
