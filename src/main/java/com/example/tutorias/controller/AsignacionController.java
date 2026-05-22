package com.example.tutorias.controller;

import com.example.tutorias.model.Asignacion;
import com.example.tutorias.model.Semestre;
import com.example.tutorias.service.AsignacionService;
import com.example.tutorias.repository.AsignacionRepository;
import com.example.tutorias.repository.TutorRepository;
import com.example.tutorias.repository.TutoradoRepository;
import com.example.tutorias.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/asignaciones")
public class AsignacionController {

    @Autowired private AsignacionService asignacionService;
    @Autowired private AsignacionRepository asignacionRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private SemestreRepository semestreRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asignaciones", asignacionService.listarTodos());
        return "asignacion/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("asignacion", new Asignacion());
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());

        // Obtener semestre activo para filtrar tutorados disponibles
        Semestre semestreActivo = semestreRepository.findFirstByActivoTrue().orElse(null);
        if (semestreActivo != null) {
            model.addAttribute("tutorados",
                tutoradoRepository.findTutoradosDisponibles(semestreActivo.getId()));
        } else {
            model.addAttribute("tutorados", tutoradoRepository.findAll());
        }

        return "asignacion/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(value = "tutorId",     required = false) Long tutorIdNew,
            @RequestParam(value = "tutoradoId",  required = false) Long tutoradoIdNew,
            @RequestParam(value = "semestreId",  required = false) Long semestreIdNew,
            @RequestParam(value = "tutor.id",    required = false) Long tutorIdOld,
            @RequestParam(value = "tutorado.id", required = false) Long tutoradoIdOld,
            @RequestParam(value = "semestre.id", required = false) Long semestreIdOld,
            @RequestParam(required = false) Long id,
            RedirectAttributes flash) {
        Long tutorId    = tutorIdNew    != null ? tutorIdNew    : tutorIdOld;
        Long tutoradoId = tutoradoIdNew != null ? tutoradoIdNew : tutoradoIdOld;
        Long semestreId = semestreIdNew != null ? semestreIdNew : semestreIdOld;
        try {
            // Validar que vengan los 3 campos
            if (tutorId == null || tutoradoId == null || semestreId == null) {
                flash.addFlashAttribute("error",
                    "Debes seleccionar tutor, tutorado y semestre.");
                return "redirect:/asignaciones/nuevo";
            }

            // Verificar duplicado exacto (mismo tutor+tutorado+semestre)
            if (id == null) {
                long duplicado = asignacionRepository
                        .countDuplicado(tutorId, tutoradoId, semestreId);
                if (duplicado > 0) {
                    flash.addFlashAttribute("error",
                        "Esta asignación ya existe para el semestre seleccionado.");
                    return "redirect:/asignaciones/nuevo";
                }

                // Verificar que el tutorado no esté ya asignado a OTRO tutor en el mismo semestre
                long yaAsignado = asignacionRepository
                        .countTutoradoEnSemestre(tutoradoId, semestreId);
                if (yaAsignado > 0) {
                    // Obtener nombre del tutorado para el mensaje
                    String nombreTutorado = tutoradoRepository.findById(tutoradoId)
                        .map(t -> t.getUsuario() != null
                            ? t.getUsuario().getNombre() + " " + t.getUsuario().getApellido()
                            : "el tutorado seleccionado")
                        .orElse("el tutorado seleccionado");
                    String nombreSemestre = semestreRepository.findById(semestreId)
                        .map(s -> s.getNombre() != null ? s.getNombre() : "ese semestre")
                        .orElse("ese semestre");
                    flash.addFlashAttribute("error",
                        "<strong>" + nombreTutorado + "</strong> ya tiene un tutor asignado en "
                        + "<strong>" + nombreSemestre + "</strong>. "
                        + "Un tutorado solo puede tener un tutor por semestre.");
                    return "redirect:/asignaciones/nuevo";
                }
            }

            // Construir y guardar
            Asignacion asignacion = (id != null)
                    ? asignacionService.buscarPorId(id).orElse(new Asignacion())
                    : new Asignacion();

            tutorRepository.findById(tutorId).ifPresent(asignacion::setTutor);
            tutoradoRepository.findById(tutoradoId).ifPresent(asignacion::setTutorado);
            semestreRepository.findById(semestreId).ifPresent(asignacion::setSemestre);
            asignacion.setActivo(true);

            asignacionRepository.save(asignacion);
            flash.addFlashAttribute("msg", "Asignación guardada correctamente.");
            return "redirect:/asignaciones";

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return id != null
                    ? "redirect:/asignaciones/editar/" + id
                    : "redirect:/asignaciones/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        asignacionService.buscarPorId(id).ifPresent(a -> model.addAttribute("asignacion", a));
        model.addAttribute("tutores", tutorRepository.findAll());
        model.addAttribute("tutorados", tutoradoRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        return "asignacion/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            asignacionService.eliminar(id);
            flash.addFlashAttribute("msg", "Asignación eliminada.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar: " + e.getMessage());
        }
        return "redirect:/asignaciones";
    }

    @GetMapping("/buscar")
    public String buscarPorTutorado(
            @RequestParam(required = false) Long idTutorado, Model model) {
        if (idTutorado != null) {
            model.addAttribute("asignaciones",
                    asignacionService.buscarPorTutorado(idTutorado));
        } else {
            model.addAttribute("asignaciones", asignacionService.listarTodos());
        }
        model.addAttribute("tutorados", tutoradoRepository.findAll());
        model.addAttribute("idTutorado", idTutorado);
        return "asignacion/buscar";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        asignacionService.buscarPorId(id).ifPresent(a -> model.addAttribute("asignacion", a));
        return "asignacion/detalle";
    }

    /**
     * JSON: devuelve tutorados disponibles (sin asignar en el semestre activo)
     * filtrados por la carrera del tutor seleccionado.
     * Llamado dinámicamente desde el formulario al elegir tutor.
     */
    @GetMapping("/tutorados-por-tutor")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> tutoradosPorTutor(
            @RequestParam Long tutorId,
            @RequestParam(required = false) Long semestreId) {

        // Obtener carrera del tutor
        Long carreraId = tutorRepository.findById(tutorId)
                .map(t -> t.getCarrera() != null ? t.getCarrera().getId() : null)
                .orElse(null);

        // Si no tiene carrera asignada, devolver todos los disponibles
        List<com.example.tutorias.model.Tutorado> tutorados;
        if (carreraId != null) {
            tutorados = tutoradoRepository.findByCarrera_Id(carreraId);
        } else {
            tutorados = tutoradoRepository.findAll();
        }

        // Si hay semestre, filtrar solo los que no están asignados en ese semestre
        final Long semId = semestreId;
        if (semId != null) {
            tutorados = tutorados.stream()
                .filter(t -> asignacionRepository.countTutoradoEnSemestre(t.getId(), semId) == 0)
                .toList();
        }

        List<Map<String, Object>> result = tutorados.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getId());
            String nombre = t.getUsuario() != null
                ? t.getUsuario().getNombre() + " " + t.getUsuario().getApellido()
                : "Tutorado #" + t.getId();
            m.put("nombre", nombre + " (" + (t.getMatricula() != null ? t.getMatricula() : "—") + ")");
            return m;
        }).toList();

        return ResponseEntity.ok(result);
    }
}