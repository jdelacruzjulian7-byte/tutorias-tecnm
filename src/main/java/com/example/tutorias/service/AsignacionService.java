package com.example.tutorias.service;

import com.example.tutorias.model.Asignacion;
import com.example.tutorias.repository.AsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionService {

	@Autowired
	private AsignacionRepository asignacionRepository;

	public List<Asignacion> listarTodos() {
		return asignacionRepository.findAll();
	}

	public Optional<Asignacion> buscarPorId(Long id) {
		return asignacionRepository.findById(id);
	}

	public Asignacion guardar(Asignacion asignacion) {
		return asignacionRepository.save(asignacion);
	}

	public void eliminar(Long id) {
		asignacionRepository.deleteById(id);
	}
	
	public List<Asignacion> buscarPorTutorado(Long idTutorado) {
	    return asignacionRepository.findByTutorado(idTutorado);
	}
}