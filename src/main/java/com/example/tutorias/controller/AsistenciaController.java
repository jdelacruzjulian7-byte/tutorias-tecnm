package com.example.tutorias.controller;

import com.example.tutorias.model.Asistencia;
import com.example.tutorias.model.Sesion;
import com.example.tutorias.model.Tutorado;
import com.example.tutorias.service.AsistenciaService;
import com.example.tutorias.repository.AsistenciaRepository;
import com.example.tutorias.repository.AsignacionRepository;
import com.example.tutorias.repository.TutoradoRepository;
import com.example.tutorias.repository.SesionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

	@Autowired
	private AsistenciaService asistenciaService;
	@Autowired
	private TutoradoRepository tutoradoRepository;
	@Autowired
	private SesionRepository sesionRepository;
	@Autowired
	private AsistenciaRepository asistenciaRepository;
	@Autowired
	private AsignacionRepository asignacionRepository;

	// ── Listado general ──
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("asistencias", asistenciaService.listarTodos());
		return "asistencia/listar";
	}

	// ── Tomar asistencia de una sesión completa ──
	@GetMapping("/sesion/{sesionId}")
	public String tomarAsistencia(@PathVariable Long sesionId, Model model, Authentication auth) {
		Sesion sesion = sesionRepository.findById(sesionId)
				.orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

		// Obtener tutorados del tutor de esta sesión
		List<Tutorado> tutorados;
		if (sesion.getTutor() != null) {
			tutorados = asignacionRepository.findByTutor_IdAndSemestre_Id(sesion.getTutor().getId(),
					sesion.getActividad() != null && sesion.getActividad().getPat() != null
							? sesion.getActividad().getPat().getSemestre().getId()
							: null)
					.stream().map(a -> a.getTutorado()).toList();
		} else {
			tutorados = tutoradoRepository.findAll();
		}

		// Para cada tutorado, buscar si ya tiene asistencia en esta sesión
		List<Asistencia> asistencias = tutorados.stream().map(t -> {
			return asistenciaRepository.findByTutorado_IdAndSesion_Id(t.getId(), sesionId).orElseGet(() -> {
				Asistencia a = new Asistencia();
				a.setTutorado(t);
				a.setSesion(sesion);
				a.setPresente(false);
				return a;
			});
		}).toList();

		model.addAttribute("sesion", sesion);
		model.addAttribute("asistencias", asistencias);
		return "asistencia/tomar";
	}

	// ── Guardar asistencia de toda la sesión de una vez ──
	@PostMapping("/sesion/{sesionId}/guardar")
	public String guardarAsistencia(@PathVariable Long sesionId,
			@RequestParam(value = "presentes", required = false) List<Long> tutoradosPresentes,
			@RequestParam java.util.Map<String, String> allParams,
			org.springframework.web.servlet.mvc.support.RedirectAttributes flash) {
		Sesion sesion = sesionRepository.findById(sesionId)
				.orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

		// Obtener todos los tutorados de la sesión
		List<Tutorado> tutorados;
		if (sesion.getTutor() != null && sesion.getActividad() != null && sesion.getActividad().getPat() != null) {
			tutorados = asignacionRepository
					.findByTutor_IdAndSemestre_Id(sesion.getTutor().getId(),
							sesion.getActividad().getPat().getSemestre().getId())
					.stream().map(a -> a.getTutorado()).toList();
		} else {
			tutorados = tutoradoRepository.findAll();
		}

		// Guardar o actualizar asistencia + observación individual de cada tutorado
		for (Tutorado t : tutorados) {
			Asistencia asistencia = asistenciaRepository.findByTutorado_IdAndSesion_Id(t.getId(), sesionId)
					.orElseGet(() -> {
						Asistencia a = new Asistencia();
						a.setTutorado(t);
						a.setSesion(sesion);
						return a;
					});
			boolean presente = tutoradosPresentes != null && tutoradosPresentes.contains(t.getId());
			asistencia.setPresente(presente);

			// Guardar observación individual si viene en el form
			String obsKey = "obs_" + t.getId();
			if (allParams.containsKey(obsKey)) {
				String obs = allParams.get(obsKey);
				asistencia.setObservacion((obs != null && !obs.isBlank()) ? obs.trim() : null);
			}
			asistenciaService.guardar(asistencia);
		}

		flash.addFlashAttribute("msg", "✅ Asistencia guardada correctamente.");
		return "redirect:/sesiones";
	}

	// ── Formulario individual (mantener para edición) ──
	@GetMapping("/nuevo")
	public String nuevo(@RequestParam(value = "sesionId", required = false) Long sesionId, Model model) {
		Asistencia asistencia = new Asistencia();
		if (sesionId != null) {
			sesionRepository.findById(sesionId).ifPresent(asistencia::setSesion);
		}
		model.addAttribute("asistencia", asistencia);
		model.addAttribute("tutorados", tutoradoRepository.findAll());
		model.addAttribute("sesiones", sesionRepository.findAll());
		return "asistencia/formulario";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Asistencia asistencia,
			@RequestParam(value = "tutorado.id", required = false) Long tutoradoId,
			@RequestParam(value = "sesion.id", required = false) Long sesionId) {
		if (tutoradoId != null)
			tutoradoRepository.findById(tutoradoId).ifPresent(asistencia::setTutorado);
		if (sesionId != null)
			sesionRepository.findById(sesionId).ifPresent(asistencia::setSesion);
		asistenciaService.guardar(asistencia);
		return "redirect:/asistencias";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		asistenciaService.buscarPorId(id).ifPresent(a -> model.addAttribute("asistencia", a));
		model.addAttribute("tutorados", tutoradoRepository.findAll());
		model.addAttribute("sesiones", sesionRepository.findAll());
		return "asistencia/formulario";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
		asistenciaService.eliminar(id);
		return "redirect:/asistencias";
	}

	// ── Endpoint JSON: detalle de asistencia de una sesión (para panel inline) ──
	@GetMapping("/sesion/{sesionId}/detalle")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> detalleJson(@PathVariable Long sesionId) {
		List<Asistencia> lista = asistenciaRepository.findBySesion_Id(sesionId);
		List<Map<String, Object>> result = lista.stream().map(a -> {
			Map<String, Object> m = new HashMap<>();
			m.put("matricula", a.getTutorado() != null ? a.getTutorado().getMatricula() : "—");
			String nombre = "—";
			if (a.getTutorado() != null && a.getTutorado().getUsuario() != null) {
				nombre = a.getTutorado().getUsuario().getNombre() + " "
						+ a.getTutorado().getUsuario().getApellido();
			}
			m.put("nombre", nombre);
			m.put("presente", Boolean.TRUE.equals(a.getPresente()));
			m.put("observacion", a.getObservacion() != null ? a.getObservacion() : "");
			return m;
		}).toList();
		return ResponseEntity.ok(result);
	}
}