package com.example.tutorias.controller;

import com.example.tutorias.model.Actividad;
import com.example.tutorias.service.ActividadService;
import com.example.tutorias.repository.PatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/actividades")
public class ActividadController {

	@Autowired
	private ActividadService actividadService;
	@Autowired
	private PatRepository patRepository;

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("actividades", actividadService.listarTodos());
		return "actividad/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(@RequestParam(value = "patId", required = false) Long patId, Model model) {
		Actividad actividad = new Actividad();
		if (patId != null) {
			patRepository.findById(patId).ifPresent(actividad::setPat);
		}
		model.addAttribute("actividad", actividad);
		model.addAttribute("pats", patRepository.findAll());
		return "actividad/formulario";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Actividad actividad,
			@RequestParam(value = "pat.id", required = false) Long patId) {
		if (patId != null) {
			patRepository.findById(patId).ifPresent(actividad::setPat);
		} else {
			actividad.setPat(null);
		}
		actividadService.guardar(actividad);
		if (actividad.getPat() != null) {
			return "redirect:/pats/detalle/" + actividad.getPat().getId();
		}
		return "redirect:/actividades";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
		actividadService.buscarPorId(id).ifPresent(a -> model.addAttribute("actividad", a));
		model.addAttribute("pats", patRepository.findAll());
		return "actividad/formulario";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
	    Long patId = actividadService.buscarPorId(id)
	            .map(a -> a.getPat() != null ? a.getPat().getId() : null)
	            .orElse(null);
	    
	    // Si pertenece a un PAT no se puede eliminar — solo editar
	    if (patId != null) {
	        return "redirect:/pats/detalle/" + patId;
	    }
	    
	    actividadService.eliminar(id);
	    return "redirect:/actividades";
	}

	@GetMapping("/detalle/{id}")
	public String detalle(@PathVariable Long id, Model model) {
		actividadService.buscarPorId(id).ifPresent(a -> model.addAttribute("actividad", a));
		return "actividad/detalle";
	}
}