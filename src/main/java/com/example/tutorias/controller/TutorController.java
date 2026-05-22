package com.example.tutorias.controller;

import com.example.tutorias.model.Tutor;
import com.example.tutorias.service.TutorService;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.UsuarioRepository;
import com.example.tutorias.repository.SemestreRepository;
import com.example.tutorias.repository.AsignacionRepository;
import com.example.tutorias.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tutores")
public class TutorController {

    @Autowired private TutorService tutorService;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private AsignacionRepository asignacionRepository;

    // Lista principal: solo activos
    @GetMapping
    public String listar(Model model) {
        List<Tutor> todos = tutorService.listarTodos();
        List<Tutor> activos = todos.stream().filter(t -> Boolean.TRUE.equals(t.getActivo())).collect(Collectors.toList());
        model.addAttribute("tutores", activos);
        model.addAttribute("totalInactivos", todos.size() - activos.size());
        return "tutor/listar";
    }

    // Lista de inactivos
    @GetMapping("/inactivos")
    public String listarInactivos(Model model) {
        List<Tutor> inactivos = tutorService.listarTodos().stream()
            .filter(t -> Boolean.FALSE.equals(t.getActivo())).collect(Collectors.toList());
        model.addAttribute("tutores", inactivos);
        return "tutor/inactivos";
    }

    // Reactivar
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutorService.buscarPorId(id).ifPresent(t -> {
            t.setActivo(true);
            tutorRepository.save(t);
        });
        flash.addFlashAttribute("msg", "✅ Tutor reactivado.");
        return "redirect:/tutores";
    }

    // Desactivar
    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutorService.buscarPorId(id).ifPresent(t -> {
            t.setActivo(false);
            tutorRepository.save(t);
        });
        flash.addFlashAttribute("msg", "Tutor desactivado. Puedes reactivarlo desde la lista de inactivos.");
        return "redirect:/tutores";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tutor", new Tutor());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findUsuariosDisponiblesComoTutor());
        return "tutor/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Tutor tutor,
            @RequestParam Long usuarioId, @RequestParam Long carreraId,
            @RequestParam(required = false) MultipartFile archivoFoto,
            RedirectAttributes flash) {
        usuarioRepository.findById(usuarioId).ifPresent(tutor::setUsuario);
        carreraRepository.findById(carreraId).ifPresent(tutor::setCarrera);
        if (archivoFoto != null && !archivoFoto.isEmpty()) {
            try {
                String nombreArchivo = System.currentTimeMillis() + "_" + archivoFoto.getOriginalFilename();
                Path ruta = Paths.get("uploads/tutores/" + nombreArchivo);
                Files.createDirectories(ruta.getParent());
                Files.write(ruta, archivoFoto.getBytes());
                tutor.setFoto("tutores/" + nombreArchivo);
            } catch (Exception e) { e.printStackTrace(); }
        }
        tutorService.guardar(tutor);
        flash.addFlashAttribute("msg", "✅ Tutor guardado correctamente.");
        return "redirect:/tutores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Tutor tutor = tutorService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        model.addAttribute("tutor", tutor);
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        // Mostrar foto actual si existe
        model.addAttribute("fotoActual", tutor.getFoto());
        return "tutor/formulario";
    }

    @Autowired private com.example.tutorias.repository.SesionRepository sesionRepository;

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        com.example.tutorias.model.Tutor tutor = tutorService.buscarPorId(id).orElse(null);
        if (tutor == null) {
            flash.addFlashAttribute("error", "El tutor no existe.");
            return "redirect:/tutores";
        }

        java.util.List<String> vinculos = new java.util.ArrayList<>();
        int asignaciones = asignacionRepository.findByTutorId(id).size();
        int sesiones = sesionRepository.findByTutor_IdOrderByFechaAsc(id).size();
        if (asignaciones > 0) vinculos.add(asignaciones + " asignación(es) de tutoría");
        if (sesiones > 0) vinculos.add(sesiones + " sesión(es) registrada(s)");

        if (!vinculos.isEmpty()) {
            String nombre = tutor.getUsuario() != null
                ? tutor.getUsuario().getNombre() + " " + tutor.getUsuario().getApellido() : "este tutor";
            flash.addFlashAttribute("error",
                "No se puede eliminar a <strong>" + nombre + "</strong> porque tiene: "
                + String.join(", ", vinculos) + ". Usa <strong>Desactivar</strong> si ya no está activo.");
            return "redirect:/tutores";
        }

        tutorService.eliminar(id);
        flash.addFlashAttribute("msg", "Tutor eliminado correctamente.");
        return "redirect:/tutores";
    }

    @GetMapping("/buscar")
    public String buscarPorSemestre(@RequestParam(required = false) Long idSemestre, Model model) {
        if (idSemestre != null) {
            model.addAttribute("tutores", tutorService.buscarPorSemestre(idSemestre));
        } else {
            model.addAttribute("tutores", tutorService.listarTodos());
        }
        model.addAttribute("semestres", semestreRepository.findAll());
        model.addAttribute("idSemestre", idSemestre);
        return "tutor/buscar";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        tutorService.buscarPorId(id).ifPresent(t -> model.addAttribute("tutor", t));
        model.addAttribute("asignaciones", asignacionRepository.findByTutorId(id));
        return "tutor/detalle";
    }
}