package com.example.tutorias.service;

import com.example.tutorias.model.Carrera;
import com.example.tutorias.model.Tutor;
import com.example.tutorias.model.Usuario;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.TutorRepository;
import com.example.tutorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorService {

	@Autowired
	private TutorRepository tutorRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CarreraRepository carreraRepository;

	public Tutor guardar(Long usuarioId, Long carreraId, boolean activo) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));
		Carrera carrera = carreraRepository.findById(carreraId)
				.orElseThrow(() -> new RuntimeException("Carrera no encontrada: " + carreraId));

		Tutor tutor = new Tutor();
		tutor.setUsuario(usuario);
		tutor.setCarrera(carrera);
		tutor.setActivo(activo);
		return tutorRepository.save(tutor);
	}

	// Guardar objeto Tutor directamente (para edición)
	public Tutor guardar(Tutor tutor) {
		return tutorRepository.save(tutor);
	}

	public void eliminar(Long id) {
		tutorRepository.deleteById(id);
	}

	public List<Tutor> buscarPorSemestre(Long idSemestre) {
		return tutorRepository.findBySemestre(idSemestre);
	}

	public List<Tutor> listarTodos() {
		return tutorRepository.findAll();
	}

	public Optional<Tutor> buscarPorId(Long id) {
		return tutorRepository.findById(id);
	}
}