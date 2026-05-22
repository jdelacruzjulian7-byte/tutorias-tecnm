package com.example.tutorias.controller;

import com.example.tutorias.model.Tutorado;
import com.example.tutorias.model.Usuario;
import com.example.tutorias.service.TutoradoService;
import com.example.tutorias.repository.AsignacionRepository;
import com.example.tutorias.repository.AsistenciaRepository;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.TutoradoRepository;
import com.example.tutorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tutorados")
public class TutoradoController {

    @Autowired private TutoradoService tutoradoService;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private AsignacionRepository asignacionRepository;
    @Autowired private AsistenciaRepository asistenciaRepository;

    // Lista principal: solo activos
    @GetMapping
    public String listar(Model model, Authentication auth) {
        List<Tutorado> todos;
        boolean esTutor = auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> "ROLE_TUTOR".equals(a.getAuthority()));
        if (esTutor) {
            todos = tutoradoService.listarPorTutorCorreo(auth.getName());
        } else {
            todos = tutoradoService.listarTodos();
        }
        List<Tutorado> activos = todos.stream()
            .filter(t -> Boolean.TRUE.equals(t.getActivo())).collect(Collectors.toList());
        model.addAttribute("tutorados", activos);
        model.addAttribute("totalInactivos", todos.size() - activos.size());
        return "tutorado/listar";
    }

    // Lista de inactivos
    @GetMapping("/inactivos")
    public String listarInactivos(Model model) {
        List<Tutorado> inactivos = tutoradoService.listarTodos().stream()
            .filter(t -> Boolean.FALSE.equals(t.getActivo())).collect(Collectors.toList());
        model.addAttribute("tutorados", inactivos);
        return "tutorado/inactivos";
    }

    // Reactivar
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutoradoService.buscarPorId(id).ifPresent(t -> {
            t.setActivo(true);
            tutoradoRepository.save(t);
        });
        flash.addFlashAttribute("msg", "✅ Tutorado reactivado.");
        return "redirect:/tutorados";
    }

    // Desactivar
    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutoradoService.buscarPorId(id).ifPresent(t -> {
            t.setActivo(false);
            tutoradoRepository.save(t);
        });
        flash.addFlashAttribute("msg", "Tutorado desactivado.");
        return "redirect:/tutorados";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tutorado", new Tutorado());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findUsuariosDisponiblesComoTutorado());
        return "tutorado/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @RequestParam(required = false) Long id,
            @RequestParam Long usuarioId,
            @RequestParam Long carreraId,
            @RequestParam String matricula,
            @RequestParam String semestreIngreso,
            @RequestParam(defaultValue = "true") boolean activo,
            @RequestParam(required = false) MultipartFile archivoFoto,
            RedirectAttributes flash) {
        try {
            if (matricula != null && !matricula.isBlank()) {
                boolean dup = tutoradoRepository.findByMatricula(matricula)
                    .filter(t -> id == null || !t.getId().equals(id)).isPresent();
                if (dup) {
                    flash.addFlashAttribute("error", "❌ La matrícula '" + matricula + "' ya está registrada.");
                    return id != null ? "redirect:/tutorados/editar/" + id : "redirect:/tutorados/nuevo";
                }
            }
            boolean usuarioDup = tutoradoRepository.findByUsuario_Id(usuarioId)
                .filter(t -> id == null || !t.getId().equals(id)).isPresent();
            if (usuarioDup) {
                flash.addFlashAttribute("error", "❌ Este usuario ya está registrado como tutorado.");
                return id != null ? "redirect:/tutorados/editar/" + id : "redirect:/tutorados/nuevo";
            }

            Tutorado tutorado = (id != null)
                ? tutoradoService.buscarPorId(id).orElse(new Tutorado())
                : new Tutorado();
            usuarioRepository.findById(usuarioId).ifPresent(tutorado::setUsuario);
            carreraRepository.findById(carreraId).ifPresent(tutorado::setCarrera);
            tutorado.setMatricula(matricula);
            tutorado.setSemestreIngreso(semestreIngreso);
            tutorado.setActivo(activo);

            if (archivoFoto != null && !archivoFoto.isEmpty()) {
                try {
                    String nombreArchivo = System.currentTimeMillis() + "_" + archivoFoto.getOriginalFilename();
                    Path ruta = Paths.get("uploads/tutorados/" + nombreArchivo);
                    Files.createDirectories(ruta.getParent());
                    Files.write(ruta, archivoFoto.getBytes());
                    tutorado.setFoto("tutorados/" + nombreArchivo);
                } catch (Exception e) { e.printStackTrace(); }
            }

            tutoradoService.guardar(tutorado);
            flash.addFlashAttribute("msg", "✅ Tutorado guardado correctamente.");
            return "redirect:/tutorados";
        } catch (Exception e) {
            flash.addFlashAttribute("error", "❌ Error al guardar: " + e.getMessage());
            return id != null ? "redirect:/tutorados/editar/" + id : "redirect:/tutorados/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Tutorado tutorado = tutoradoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Tutorado no encontrado"));
        model.addAttribute("tutorado", tutorado);
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("fotoActual", tutorado.getFoto());
        List<Usuario> usuarios = new ArrayList<>(usuarioRepository.findUsuariosDisponiblesComoTutorado());
        if (tutorado.getUsuario() != null && !usuarios.contains(tutorado.getUsuario()))
            usuarios.add(tutorado.getUsuario());
        model.addAttribute("usuarios", usuarios);
        return "tutorado/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        com.example.tutorias.model.Tutorado tutorado = tutoradoService.buscarPorId(id).orElse(null);
        if (tutorado == null) {
            flash.addFlashAttribute("error", "El tutorado no existe.");
            return "redirect:/tutorados";
        }

        java.util.List<String> vinculos = new java.util.ArrayList<>();
        int asignaciones = asignacionRepository.findByTutorado(id).size();
        int asistencias = asistenciaRepository.findByTutorado_Id(id).size();
        if (asignaciones > 0) vinculos.add(asignaciones + " asignación(es) de tutoría");
        if (asistencias > 0) vinculos.add(asistencias + " registro(s) de asistencia");

        if (!vinculos.isEmpty()) {
            String nombre = tutorado.getUsuario() != null
                ? tutorado.getUsuario().getNombre() + " " + tutorado.getUsuario().getApellido() : "este tutorado";
            flash.addFlashAttribute("error",
                "No se puede eliminar a <strong>" + nombre + "</strong> porque tiene: "
                + String.join(", ", vinculos) + ". Usa <strong>Desactivar</strong> si ya no está activo.");
            return "redirect:/tutorados";
        }

        tutoradoService.eliminar(id);
        flash.addFlashAttribute("msg", "Tutorado eliminado correctamente.");
        return "redirect:/tutorados";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Tutorado tutorado = tutoradoService.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Tutorado no encontrado"));
        model.addAttribute("tutorado", tutorado);
        model.addAttribute("asignaciones", List.of());
        return "tutorado/detalle";
    }
}