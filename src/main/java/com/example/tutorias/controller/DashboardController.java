package com.example.tutorias.controller;

import com.example.tutorias.model.*;
import com.example.tutorias.repository.*;
import com.example.tutorias.service.AsistenciaService;
import com.example.tutorias.service.PatService;
import com.example.tutorias.service.TutoradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private TutoradoService tutoradoService;
    @Autowired private PatService patService;
    @Autowired private AsignacionRepository asignacionRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private AsistenciaService asistenciaService;
    @Autowired private DocumentoRepository documentoRepository;
    @Autowired private ActividadRepository actividadRepository;
    @Autowired private SesionRepository sesionRepository;

    // ── ADMIN ─────────────────────────────────────────────────
    @GetMapping("/admin")
    public String adminDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);
        model.addAttribute("usuario", u);
        model.addAttribute("semestreActivo", activo);
        List<Tutor> listaTutores = tutorRepository.findAll();
        List<Tutorado> listaTutorados = tutoradoRepository.findAll();
        List<Pat> listaPats = patService.listarTodos();

        model.addAttribute("tutor", listaTutores);
        model.addAttribute("tutorado", listaTutorados);
        model.addAttribute("pats", listaPats);
        model.addAttribute("totalTutores", listaTutores.size());
        model.addAttribute("totalTutorados", listaTutorados.size());
        model.addAttribute("totalCarreras", carreraRepository.findAll().size());
        model.addAttribute("totalPats", listaPats.size());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("documentos", documentoRepository.findAll());
        return "dashboard/admin";
    }

    // ── TUTOR ─────────────────────────────────────────────────
    @GetMapping("/tutor")
    public String tutorDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Tutor tutor = tutorRepository.findByUsuario_Id(u.getId()).orElse(null);
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);

        List<Tutorado> misTutorados = (tutor != null)
            ? tutoradoService.listarPorTutorYSemestre(tutor.getId(), activo != null ? activo.getId() : null)
            : List.of();

        // PAT de la carrera del tutor
        Pat miPat = null;
        if (tutor != null && tutor.getCarrera() != null && activo != null) {
            miPat = patService.listarTodos().stream()
                .filter(p -> !Boolean.TRUE.equals(p.getEsGeneral())
                    && p.getCarrera() != null
                    && p.getCarrera().getId().equals(tutor.getCarrera().getId())
                    && "APROBADO".equals(p.getEstado()))
                .findFirst().orElse(null);
        }

        // Sesiones del tutor
        List<Sesion> misSesiones = (tutor != null)
            ? sesionRepository.findByTutor_IdOrderByFechaAsc(tutor.getId())
            : List.of();

        model.addAttribute("usuario", u);
        model.addAttribute("tutor", tutor);
        model.addAttribute("semestreActivo", activo);
        model.addAttribute("tutorados", misTutorados);
        model.addAttribute("totalTutorados", misTutorados.size());
        model.addAttribute("miPat", miPat);
        model.addAttribute("misSesiones", misSesiones.stream().limit(5).collect(Collectors.toList()));
        return "dashboard/tutor";
    }

    // ── TUTORADO ──────────────────────────────────────────────
    @GetMapping("/tutorado")
    public String tutoradoDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Tutorado tutorado = tutoradoRepository.findByUsuario_Id(u.getId()).orElse(null);
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);

        double porcentaje = 0d;
        Tutor miTutor = null;
        if (tutorado != null && activo != null) {
            porcentaje = asistenciaService.calcularPorcentaje(tutorado.getId(), activo.getId());
            miTutor = asignacionRepository.findTutorByTutoradoAndSemestre(tutorado.getId(), activo.getId()).orElse(null);
        }

        model.addAttribute("usuario", u);
        model.addAttribute("tutorado", tutorado);
        model.addAttribute("semestreActivo", activo);
        model.addAttribute("porcentajeAsistencia", porcentaje);
        model.addAttribute("apto", porcentaje >= 80d);
        model.addAttribute("miTutor", miTutor);
        model.addAttribute("misDocumentos", tutorado != null ? documentoRepository.findByUsuario_Id(u.getId()) : List.of());
        model.addAttribute("nivelAsistencia", porcentaje >= 80d ? "success" : porcentaje >= 60d ? "warn" : "danger");
        // Para el botón de carnet directo desde el dashboard
        model.addAttribute("usuarioId", u.getId());
        return "dashboard/tutorado";
    }

    // ── COORDINADOR ───────────────────────────────────────────
    @GetMapping("/coordinador")
    public String coordinadorDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);

        // Coordinador ve PATs de carrera (no generales) que estén aprobados o pendientes
        List<Pat> pats = patService.listarTodos().stream()
            .filter(p -> !Boolean.TRUE.equals(p.getEsGeneral()))
            .collect(Collectors.toList());

        // PATs pendientes (para que el coordinador los edite/apruebe)
        List<Pat> patsPendientes = pats.stream()
            .filter(p -> "PENDIENTE".equals(p.getEstado()))
            .collect(Collectors.toList());

        // Stats: semanas completadas del PAT activo
        long semanasCompletadas = activo != null
            ? actividadRepository.findAll().stream()
                .filter(a -> a.getPat() != null && !Boolean.TRUE.equals(a.getPat().getEsGeneral())
                    && a.getPat().getSemestre() != null && a.getPat().getSemestre().getId().equals(activo.getId()))
                .mapToInt(a -> a.getSemana() != null ? 1 : 0).sum()
            : 0;

        model.addAttribute("usuario", u);
        model.addAttribute("semestreActivo", activo);
        model.addAttribute("pats", pats);
        model.addAttribute("patsPendientes", patsPendientes);
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("semanasCompletadas", Math.min(semanasCompletadas, 13));
        model.addAttribute("totalSesiones", sesionRepository.findAll().size());
        return "dashboard/coordinador";
    }

    // ── JEFE ACADÉMICO ────────────────────────────────────────
    @GetMapping("/jefe")
    public String jefeDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);

        // Jefe solo ve datos básicos, no edita PAT
        List<Tutor> tutores = tutorRepository.findAll().stream()
            .filter(t -> Boolean.TRUE.equals(t.getActivo())).collect(Collectors.toList());
        List<Tutorado> tutorados = tutoradoRepository.findAll().stream()
            .filter(t -> Boolean.TRUE.equals(t.getActivo())).collect(Collectors.toList());

        // PATs aprobados (solo lectura para jefe)
        List<Pat> patsAprobados = patService.listarTodos().stream()
            .filter(p -> "APROBADO".equals(p.getEstado()) && !Boolean.TRUE.equals(p.getEsGeneral()))
            .collect(Collectors.toList());

        model.addAttribute("usuario", u);
        model.addAttribute("semestreActivo", activo);
        model.addAttribute("tutores", tutores);
        model.addAttribute("tutorados", tutorados);
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("patsAprobados", patsAprobados);
        model.addAttribute("tutoresCarrera", tutores.size());
        model.addAttribute("tutoradosCarrera", tutorados.size());
        return "dashboard/jefe";
    }

    // ── SUBDIRECTOR ───────────────────────────────────────────
    @GetMapping("/subdirector")
    public String subdirectorDashboard(Model model, Authentication auth) {
        Usuario u = usuarioRepository.findByCorreo(auth.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Semestre activo = semestreRepository.findFirstByActivoTrue().orElse(null);

        long docsPendientes = documentoRepository.findAll().stream()
            .filter(d -> "CONSTANCIA".equals(d.getTipo()) || "OFICIO_TERMINO".equals(d.getTipo())).count();

        model.addAttribute("usuario", u);
        model.addAttribute("semestreActivo", activo);
        model.addAttribute("documentos", documentoRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("totalTutores", tutorRepository.count());
        model.addAttribute("totalTutorados", tutoradoRepository.count());
        model.addAttribute("docsPendientes", docsPendientes);
        return "dashboard/subdirector";
    }
}
