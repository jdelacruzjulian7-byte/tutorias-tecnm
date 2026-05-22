package com.example.tutorias.service;

import com.example.tutorias.model.Actividad;
import com.example.tutorias.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    public List<Actividad> listarTodos() {
        return actividadRepository.findAll();
    }

    public Optional<Actividad> buscarPorId(Long id) {
        return actividadRepository.findById(id);
    }

    public void guardar(Actividad actividad) {
        actividadRepository.save(actividad);
    }

    public void eliminar(Long id) {
        actividadRepository.deleteById(id);
    }

    public List<Actividad> buscarPorFecha(LocalDate fecha) {
        return actividadRepository.findByFecha(fecha);
    }
}