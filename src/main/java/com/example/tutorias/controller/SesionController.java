package com.example.tutorias.controller;

import com.example.tutorias.model.Sesion;
import com.example.tutorias.service.SesionService;
import com.example.tutorias.repository.ActividadRepository;
import com.example.tutorias.repository.AsistenciaRepository;
import com.example.tutorias.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sesiones")
public class SesionController {

	@Autowired
	private SesionService sesionService;
	@Autowired
	private ActividadRepository actividadRepository;
	@Autowired
	private TutorRepository tutorRepository;
	@Autowired
	private AsistenciaRepository asistenciaRepository;

	@GetMapping
	public String listar(Model model, Authentication auth) {
		boolean esTutor = auth != null
				&& auth.getAuthorities().stream().anyMatch(a -> "ROLE_TUTOR".equals(a.getAuthority()));

		java.util.List<Sesion> sesiones;
		if (esTutor) {
			String correo = auth.getName();
			sesiones = tutorRepository.findByUsuario_Correo(correo)
					.map(t -> sesionService.listarPorTutor(t.getId()))
					.orElse(java.util.List.of());
		} else {
			sesiones = sesionService.listarTodos();
		}
		model.addAttribute("sesiones", sesiones);

		// Mapa sesionId -> {presentes, total} para mostrar resumen en tabla
		java.util.Map<Long, int[]> statsAsistencia = new java.util.HashMap<>();
		for (Sesion s : sesiones) {
			java.util.List<com.example.tutorias.model.Asistencia> lista =
				asistenciaRepository.findBySesion_Id(s.getId());
			int presentes = (int) lista.stream().filter(a -> Boolean.TRUE.equals(a.getPresente())).count();
			statsAsistencia.put(s.getId(), new int[]{presentes, lista.size()});
		}
		model.addAttribute("statsAsistencia", statsAsistencia);

		return "sesion/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(@RequestParam(value = "actividadId", required = false) Long actividadId, Model model,
			Authentication auth) {
		Sesion sesion = new Sesion();

		// Si es tutor, preseleccionar su tutor automáticamente
		if (auth != null) {
			tutorRepository.findByUsuario_Correo(auth.getName()).ifPresent(sesion::setTutor);
		}

		if (actividadId != null) {
			actividadRepository.findById(actividadId).ifPresent(sesion::setActividad);
		}

		model.addAttribute("sesion", sesion);
		model.addAttribute("actividades", actividadRepository.findAll());
		model.addAttribute("tutores", tutorRepository.findAll());
		return "sesion/formulario";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Sesion sesion,
			@RequestParam(value = "actividad.id", required = false) Long actividadId,
			@RequestParam(value = "tutor.id", required = false) Long tutorId) {
		if (actividadId != null) {
			actividadRepository.findById(actividadId).ifPresent(sesion::setActividad);
		}
		if (tutorId != null) {
			tutorRepository.findById(tutorId).ifPresent(sesion::setTutor);
		}
		sesionService.guardar(sesion);
		return "redirect:/sesiones";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		sesionService.buscarPorId(id).ifPresent(s -> model.addAttribute("sesion", s));
		model.addAttribute("actividades", actividadRepository.findAll());
		model.addAttribute("tutores", tutorRepository.findAll());
		return "sesion/formulario";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
		com.example.tutorias.model.Sesion sesion = sesionService.buscarPorId(id).orElse(null);
		if (sesion == null) {
			flash.addFlashAttribute("error", "La sesión no existe.");
			return "redirect:/sesiones";
		}

		int asistencias = asistenciaRepository.findBySesion_Id(id).size();
		if (asistencias > 0) {
			String nombre = sesion.getNombre() != null ? sesion.getNombre() : "esta sesión";
			flash.addFlashAttribute("error",
				"No se puede eliminar <strong>" + nombre + "</strong> porque tiene "
				+ asistencias + " registro(s) de asistencia. Primero elimina las asistencias si deseas continuar.");
			return "redirect:/sesiones";
		}

		sesionService.eliminar(id);
		flash.addFlashAttribute("msg", "Sesión eliminada correctamente.");
		return "redirect:/sesiones";
	}

	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable Long id, Model model) {
		sesionService.buscarPorId(id).ifPresent(s -> model.addAttribute("sesion", s));
		return "sesion/detalle";
	}
}