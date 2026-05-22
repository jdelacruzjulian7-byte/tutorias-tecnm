package com.example.tutorias.controller;

import com.example.tutorias.model.Tutor;
import com.example.tutorias.model.Tutorado;
import com.example.tutorias.model.Usuario;
import com.example.tutorias.repository.AsignacionRepository;
import com.example.tutorias.repository.AsistenciaRepository;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.DocumentoRepository;
import com.example.tutorias.repository.RolRepository;
import com.example.tutorias.repository.SemestreRepository;
import com.example.tutorias.repository.SesionRepository;
import com.example.tutorias.repository.TutorRepository;
import com.example.tutorias.repository.TutoradoRepository;
import com.example.tutorias.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private RolRepository rolRepository;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private AsignacionRepository asignacionRepository;
    @Autowired private AsistenciaRepository asistenciaRepository;
    @Autowired private SesionRepository sesionRepository;
    @Autowired private DocumentoRepository documentoRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ── Lista principal: solo activos ──
    @GetMapping
    public String listar(Model model) {
        List<Usuario> todos = usuarioService.findAll();
        // Activos: sin registro en tutor/tutorado O con activo=true
        List<Usuario> activos = todos.stream().filter(u -> {
            var tutor = tutorRepository.findByUsuario_Id(u.getId()).orElse(null);
            var tutorado = tutoradoRepository.findByUsuario_Id(u.getId()).orElse(null);
            if (tutor != null) return Boolean.TRUE.equals(tutor.getActivo());
            if (tutorado != null) return Boolean.TRUE.equals(tutorado.getActivo());
            return true; // admin, jefe, coordinador, subdirector siempre activos
        }).collect(Collectors.toList());

        model.addAttribute("usuarios", activos);
        model.addAttribute("totalInactivos", todos.size() - activos.size());
        return "usuario/listar";
    }

    // ── Lista de inactivos ──
    @GetMapping("/inactivos")
    public String listarInactivos(Model model) {
        List<Usuario> inactivos = usuarioService.findAll().stream().filter(u -> {
            var tutor = tutorRepository.findByUsuario_Id(u.getId()).orElse(null);
            var tutorado = tutoradoRepository.findByUsuario_Id(u.getId()).orElse(null);
            if (tutor != null) return Boolean.FALSE.equals(tutor.getActivo());
            if (tutorado != null) return Boolean.FALSE.equals(tutorado.getActivo());
            return false;
        }).collect(Collectors.toList());
        model.addAttribute("usuarios", inactivos);
        return "usuario/inactivos";
    }

    // ── Reactivar usuario ──
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutorRepository.findByUsuario_Id(id).ifPresent(t -> {
            t.setActivo(true);
            tutorRepository.save(t);
        });
        tutoradoRepository.findByUsuario_Id(id).ifPresent(t -> {
            t.setActivo(true);
            tutoradoRepository.save(t);
        });
        flash.addFlashAttribute("msg", "Usuario reactivado correctamente.");
        return "redirect:/usuarios";
    }

    // ── Desactivar (soft delete) ──
    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable Long id, RedirectAttributes flash) {
        tutorRepository.findByUsuario_Id(id).ifPresent(t -> {
            t.setActivo(false);
            tutorRepository.save(t);
        });
        tutoradoRepository.findByUsuario_Id(id).ifPresent(t -> {
            t.setActivo(false);
            tutoradoRepository.save(t);
        });
        flash.addFlashAttribute("msg", "Usuario desactivado. Puedes reactivarlo desde la lista de inactivos.");
        return "redirect:/usuarios";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        model.addAttribute("tutorExistente", null);
        model.addAttribute("tutoradoExistente", null);
        return "usuario/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semestres", semestreRepository.findAll());
        model.addAttribute("tutorExistente", tutorRepository.findByUsuario_Id(id).orElse(null));
        model.addAttribute("tutoradoExistente", tutoradoRepository.findByUsuario_Id(id).orElse(null));
        return "usuario/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
            @RequestParam(value = "rol.id", required = false) Long rolId,
            @RequestParam(value = "carreraIdTutor", required = false) Long carreraIdTutor,
            @RequestParam(value = "carreraIdTutorado", required = false) Long carreraIdTutorado,
            @RequestParam(value = "matricula", required = false) String matricula,
            @RequestParam(value = "semestreIngreso", required = false) String semestreIngreso,
            @RequestParam(value = "activoTutor", required = false, defaultValue = "true") boolean activoTutor,
            @RequestParam(value = "activoTutorado", required = false, defaultValue = "true") boolean activoTutorado,
            @RequestParam(value = "archivoFotoTutor", required = false) MultipartFile archivoFotoTutor,
            @RequestParam(value = "archivoFotoTutorado", required = false) MultipartFile archivoFotoTutorado,
            RedirectAttributes flash, Model model) {

        if (rolId != null) rolRepository.findById(rolId).ifPresent(usuario::setRol);

        try {
            Usuario guardado = usuarioService.guardar(usuario);
            String nombreRol = (guardado.getRol() != null) ? guardado.getRol().getNombre().toUpperCase() : "";

            if ("TUTOR".equals(nombreRol)) {
                sincronizarTutor(guardado, carreraIdTutor, activoTutor, archivoFotoTutor);
            } else if ("TUTORADO".equals(nombreRol)) {
                sincronizarTutorado(guardado, carreraIdTutorado, matricula, semestreIngreso, activoTutorado, archivoFotoTutorado);
            }

            flash.addFlashAttribute("msg", "✅ Usuario guardado correctamente.");
            return "redirect:/usuarios";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuario", usuario);
            model.addAttribute("roles", rolRepository.findAll());
            model.addAttribute("carreras", carreraRepository.findAll());
            model.addAttribute("semestres", semestreRepository.findAll());
            model.addAttribute("tutorExistente", null);
            model.addAttribute("tutoradoExistente", null);
            return "usuario/formulario";
        }
    }

    // Eliminar real — verifica dependencias antes de borrar
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        Usuario usuario = usuarioService.findById(id).orElse(null);
        if (usuario == null) {
            flash.addFlashAttribute("error", "El usuario no existe.");
            return "redirect:/usuarios";
        }

        java.util.List<String> vinculos = new java.util.ArrayList<>();

        // Verificar vínculos de tutor
        tutorRepository.findByUsuario_Id(id).ifPresent(t -> {
            int asignaciones = asignacionRepository.findByTutorId(t.getId()).size();
            int sesiones     = sesionRepository.findByTutor_IdOrderByFechaAsc(t.getId()).size();
            if (asignaciones > 0)
                vinculos.add(asignaciones + " asignación(es) de tutoría");
            if (sesiones > 0)
                vinculos.add(sesiones + " sesión(es) registrada(s)");
        });

        // Verificar vínculos de tutorado
        tutoradoRepository.findByUsuario_Id(id).ifPresent(t -> {
            int asignaciones = asignacionRepository.findByTutorado(t.getId()).size();
            int asistencias  = asistenciaRepository.findByTutorado_Id(t.getId()).size();
            if (asignaciones > 0)
                vinculos.add(asignaciones + " asignación(es) de tutoría");
            if (asistencias > 0)
                vinculos.add(asistencias + " registro(s) de asistencia");
        });

        // Verificar documentos del usuario
        int documentos = documentoRepository.findByUsuario_Id(id).size();
        if (documentos > 0)
            vinculos.add(documentos + " documento(s) subido(s)");

        if (!vinculos.isEmpty()) {
            String lista = String.join(", ", vinculos);
            flash.addFlashAttribute("error",
                "No se puede eliminar a <strong>" + usuario.getNombre() + " " + usuario.getApellido() + "</strong> " +
                "porque tiene vínculos activos: " + lista + ". " +
                "Si ya no está activo, usa <strong>Desactivar</strong> en su lugar.");
            return "redirect:/usuarios";
        }

        // Sin vínculos → eliminar en cascada segura
        tutorRepository.findByUsuario_Id(id).ifPresent(t -> tutorRepository.deleteById(t.getId()));
        tutoradoRepository.findByUsuario_Id(id).ifPresent(t -> tutoradoRepository.deleteById(t.getId()));
        documentoRepository.deleteAll(documentoRepository.findByUsuario_Id(id));
        usuarioService.eliminar(id);

        flash.addFlashAttribute("msg",
            "Usuario <strong>" + usuario.getNombre() + " " + usuario.getApellido() + "</strong> eliminado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        model.addAttribute("tutorExistente", tutorRepository.findByUsuario_Id(id).orElse(null));
        model.addAttribute("tutoradoExistente", tutoradoRepository.findByUsuario_Id(id).orElse(null));
        return "usuario/detalle";
    }

    private void sincronizarTutor(Usuario usuario, Long carreraId, boolean activo, MultipartFile foto) {
        if (carreraId == null) return;
        Tutor tutor = tutorRepository.findByUsuario_Id(usuario.getId()).orElseGet(Tutor::new);
        tutor.setUsuario(usuario);
        carreraRepository.findById(carreraId).ifPresent(tutor::setCarrera);
        tutor.setActivo(activo);
        String fotoGuardada = guardarFoto(foto, "tutores");
        if (fotoGuardada != null) tutor.setFoto(fotoGuardada);
        tutorRepository.save(tutor);
    }

    private void sincronizarTutorado(Usuario usuario, Long carreraId, String matricula,
            String semestreIngreso, boolean activo, MultipartFile foto) {
        if (carreraId == null || matricula == null || matricula.isBlank()) return;
        Tutorado tutorado = tutoradoRepository.findByUsuario_Id(usuario.getId()).orElseGet(Tutorado::new);
        tutorado.setUsuario(usuario);
        carreraRepository.findById(carreraId).ifPresent(tutorado::setCarrera);
        tutorado.setMatricula(matricula);
        tutorado.setSemestreIngreso(semestreIngreso);
        tutorado.setActivo(activo);
        String fotoGuardada = guardarFoto(foto, "tutorados");
        if (fotoGuardada != null) tutorado.setFoto(fotoGuardada);
        tutoradoRepository.save(tutorado);
    }

    private String guardarFoto(MultipartFile foto, String subcarpeta) {
        if (foto == null || foto.isEmpty()) return null;
        try {
            String nombreArchivo = System.currentTimeMillis() + "_" +
                (foto.getOriginalFilename() != null ? foto.getOriginalFilename().replaceAll("\\s+", "_") : "foto.jpg");
            Path destino = Paths.get(uploadDir, subcarpeta, nombreArchivo);
            Files.createDirectories(destino.getParent());
            Files.write(destino, foto.getBytes());
            return subcarpeta + "/" + nombreArchivo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}