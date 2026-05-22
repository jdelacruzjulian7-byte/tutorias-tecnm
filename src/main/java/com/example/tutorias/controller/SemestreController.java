package com.example.tutorias.controller;

import com.example.tutorias.model.Semestre;
import com.example.tutorias.service.SemestreService;
import com.example.tutorias.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/semestres")
public class SemestreController {

    @Autowired private SemestreService semestreService;
    @Autowired private SemestreRepository semestreRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("semestres", semestreService.listarTodos());
        return "semestre/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("semestre", new Semestre());
        model.addAttribute("anioActual", java.time.Year.now().getValue());
        return "semestre/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(required = false) Long id,
            @RequestParam String periodo,
            @RequestParam Integer anio,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(defaultValue = "false") boolean activo,
            RedirectAttributes flash,
            Model model) {

        // Validar periodo válido
        if (!"ENE_JUL".equals(periodo) && !"AGO_DIC".equals(periodo)) {
            flash.addFlashAttribute("error", "Periodo inválido. Solo se permiten ENE_JUL o AGO_DIC.");
            return id != null ? "redirect:/semestres/editar/" + id : "redirect:/semestres/nuevo";
        }

        // Validar año razonable
        if (anio < 2000 || anio > 2100) {
            flash.addFlashAttribute("error", "El año debe estar entre 2000 y 2100.");
            return id != null ? "redirect:/semestres/editar/" + id : "redirect:/semestres/nuevo";
        }

        // Verificar duplicado (mismo periodo + año, excluyendo el actual en edición)
        Optional<Semestre> existente = semestreRepository.findByPeriodoAndAnio(periodo, anio);
        if (existente.isPresent() && (id == null || !existente.get().getId().equals(id))) {
            String nombreDup = ("ENE_JUL".equals(periodo) ? "Enero-Julio " : "Agosto-Diciembre ") + anio;
            flash.addFlashAttribute("error",
                "Ya existe el semestre <strong>" + nombreDup + "</strong>. No pueden repetirse periodo y año.");
            return id != null ? "redirect:/semestres/editar/" + id : "redirect:/semestres/nuevo";
        }

        // Construir el semestre
        Semestre semestre = (id != null)
            ? semestreService.buscarPorId(id).orElse(new Semestre())
            : new Semestre();

        semestre.setPeriodo(periodo);
        semestre.setAnio(anio);
        semestre.generarNombre();
        semestre.setActivo(activo);

        if (fechaInicio != null && !fechaInicio.isBlank()) {
            semestre.setFechaInicio(java.time.LocalDate.parse(fechaInicio));
        }
        if (fechaFin != null && !fechaFin.isBlank()) {
            semestre.setFechaFin(java.time.LocalDate.parse(fechaFin));
        }

        semestreService.guardar(semestre);
        flash.addFlashAttribute("msg", "✅ Semestre <strong>" + semestre.getNombre() + "</strong> guardado correctamente.");
        return "redirect:/semestres";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        semestreService.buscarPorId(id).ifPresent(s -> {
            model.addAttribute("semestre", s);
        });
        model.addAttribute("anioActual", java.time.Year.now().getValue());
        return "semestre/formulario";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        semestreService.buscarPorId(id).ifPresent(s -> model.addAttribute("semestre", s));
        return "semestre/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        semestreService.eliminar(id);
        flash.addFlashAttribute("msg", "Semestre eliminado.");
        return "redirect:/semestres";
    }
}