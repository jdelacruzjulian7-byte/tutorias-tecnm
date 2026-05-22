package com.example.tutorias.service;

import com.example.tutorias.model.Carrera;
import com.example.tutorias.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    public List<Carrera> listarTodos() {
        return carreraRepository.findAll();
    }

    public Optional<Carrera> buscarPorId(Long id) {
        return carreraRepository.findById(id);
    }

    public Carrera guardar(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public void eliminar(Long id) {
        carreraRepository.deleteById(id);
    }
}