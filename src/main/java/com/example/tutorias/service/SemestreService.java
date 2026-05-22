package com.example.tutorias.service;

import com.example.tutorias.model.Semestre;
import com.example.tutorias.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemestreService {

    @Autowired
    private SemestreRepository semestreRepository;

    public List<Semestre> listarTodos() {
        return semestreRepository.findAll();
    }

    public Optional<Semestre> buscarPorId(Long id) {
        return semestreRepository.findById(id);
    }

    public Semestre guardar(Semestre semestre) {
        return semestreRepository.save(semestre);
    }

    public void eliminar(Long id) {
        semestreRepository.deleteById(id);
    }
}