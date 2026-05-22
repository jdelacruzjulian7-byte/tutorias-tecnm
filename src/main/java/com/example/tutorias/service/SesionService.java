package com.example.tutorias.service;

import com.example.tutorias.model.Sesion;
import com.example.tutorias.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SesionService {

	@Autowired
	private SesionRepository sesionRepository;

	public List<Sesion> listarTodos() {
		return sesionRepository.findAll();
	}

	public Optional<Sesion> buscarPorId(Long id) {
		return sesionRepository.findById(id);
	}

	public Sesion guardar(Sesion sesion) {
		return sesionRepository.save(sesion);
	}

	public void eliminar(Long id) {
		sesionRepository.deleteById(id);
	}

	// ← nuevo
	public List<Sesion> listarPorTutor(Long tutorId) {
		return sesionRepository.findByTutor_IdOrderByFechaAsc(tutorId);
	}
}