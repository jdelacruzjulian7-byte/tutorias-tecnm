package com.example.tutorias.service;

import com.example.tutorias.model.Documento;
import com.example.tutorias.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    public List<Documento> listarTodos() {
        return documentoRepository.findAll();
    }

    public Optional<Documento> buscarPorId(Long id) {
        return documentoRepository.findById(id);
    }

    public Documento guardar(Documento documento) {
        return documentoRepository.save(documento);
    }

    public void eliminar(Long id) {
        documentoRepository.deleteById(id);
    }
}