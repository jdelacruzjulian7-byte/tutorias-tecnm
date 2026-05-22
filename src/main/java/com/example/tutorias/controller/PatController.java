package com.example.tutorias.controller;

import com.example.tutorias.model.Actividad;
import com.example.tutorias.model.Pat;
import com.example.tutorias.repository.ActividadRepository;
import com.example.tutorias.repository.AsistenciaRepository;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.PatRepository;
import com.example.tutorias.repository.SemestreRepository;
import com.example.tutorias.repository.SesionRepository;
import com.example.tutorias.repository.TutorRepository;
import com.example.tutorias.service.PatService;

import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pats")
public class PatController {

    @Autowired private PatService patService;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private ActividadRepository actividadRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private PatRepository patRepository;
    @Autowired private SesionRepository sesionRepository;
    @Autowired private AsistenciaRepository asistenciaRepository;

    // ── Listado ──
    @GetMapping
    public String listar(Model model, Authentication auth) {
        // Admin, Coordinador y demás roles ven todos los PATs
        // El coordinador gestiona todos los PATs de carrera
        List<Pat> pats = patService.listarTodos();
        model.addAttribute("pats", pats);
        return "pat/listar";
    }

    // ── Nuevo PAT General (solo Admin / Desarrollo Académico) ──
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Pat pat = new Pat();
        pat.setEsGeneral(true); // por defecto se crea como General
        model.addAttribute("pat", pat);
        model.addAttribute("semestres", semestreRepository.findAll());
        return "pat/formulario";
    }

    // ── Guardar PAT (General o edición) ──
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pat pat,
                          @RequestParam(value = "semestre.id", required = false) Long semestreId,
                          @RequestParam(value = "esGeneral", required = false) Boolean esGeneral,
                          RedirectAttributes flash, Model model) {
        // Resolver semestre
        com.example.tutorias.model.Semestre sem = null;
        if (semestreId != null) {
            sem = semestreRepository.findById(semestreId).orElse(null);
            pat.setSemestre(sem);
        }

        // Si es General, validar que no exista otro PAT General para ese semestre
        if (Boolean.TRUE.equals(esGeneral)) {
            pat.setEsGeneral(true);
            pat.setCarrera(null);
            // Versión automática = nombre del semestre
            pat.setVersion(sem != null ? sem.getNombre() : "General");

            // Validar duplicado (solo en creación)
            if (pat.getId() == null && sem != null) {
                boolean yaExiste = patService.listarTodos().stream()
                    .anyMatch(p -> Boolean.TRUE.equals(p.getEsGeneral())
                        && p.getSemestre() != null
                        && p.getSemestre().getId().equals(semestreId));
                if (yaExiste) {
                    model.addAttribute("pat", pat);
                    model.addAttribute("semestres", semestreRepository.findAll());
                    model.addAttribute("error", "Ya existe un PAT General para el semestre '" + sem.getNombre() + "'. Solo puede haber uno por semestre.");
                    return "pat/formulario";
                }
            }
        } else {
            pat.setEsGeneral(false);
        }

        // Estado por defecto PENDIENTE si es nuevo
        if (pat.getId() == null) pat.setEstado("PENDIENTE");

        patService.guardar(pat);
        flash.addFlashAttribute("mensaje", "PAT guardado correctamente.");
        return "redirect:/pats";
    }

    // ── Editar PAT (campos básicos) ──
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        patService.buscarPorId(id).ifPresent(p -> model.addAttribute("pat", p));
        model.addAttribute("semestres", semestreRepository.findAll());
        return "pat/formulario";
    }

    // ── Asignar PAT General a una carrera (coordinador lo personaliza) ──
    @GetMapping("/asignar/{id}")
    public String mostrarAsignar(@PathVariable Long id, Model model) {
        Pat pat = patService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("PAT no encontrado"));
        if (!Boolean.TRUE.equals(pat.getEsGeneral())) {
            return "redirect:/pats";
        }
        model.addAttribute("pat", pat);
        model.addAttribute("carreras", carreraRepository.findAll());
        return "pat/asignar";
    }

    @PostMapping("/asignar/{id}")
    public String procesarAsignar(@PathVariable Long id,
                                   @RequestParam("carrera.id") Long carreraId,
                                   RedirectAttributes flash) {
        Pat original = patService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("PAT no encontrado"));

        // Clonar el PAT General como PAT de carrera
        Pat copia = new Pat();
        copia.setVersion(original.getVersion());
        copia.setEstado("PENDIENTE");
        copia.setEsGeneral(false);
        copia.setSemestre(original.getSemestre());
        copia.setActivo(true);
        carreraRepository.findById(carreraId).ifPresent(copia::setCarrera);

        Pat guardado = patRepository.save(copia);

        // Copiar las 13 actividades del PAT General al PAT de carrera
        List<Actividad> actividadesOriginales = actividadRepository.findByPat_IdOrderBySemanaAsc(id);
        for (Actividad a : actividadesOriginales) {
            Actividad nueva = new Actividad();
            nueva.setSemana(a.getSemana());
            nueva.setTema(a.getTema());
            nueva.setTipo(a.getTipo());
            nueva.setObjetivo(a.getObjetivo());
            nueva.setRecursos(a.getRecursos());
            nueva.setPonente(a.getPonente());
            nueva.setFecha(a.getFecha());
            nueva.setActivo(true);
            nueva.setPat(guardado);
            actividadRepository.save(nueva);
        }

        flash.addFlashAttribute("mensaje",
                "PAT asignado a la carrera correctamente. El coordinador ya puede editarlo.");
        return "redirect:/pats/detalle/" + guardado.getId();
    }

    // ── Detalle ──
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, Authentication auth) {
        Pat pat = patService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("PAT no encontrado: " + id));
        List<Actividad> actividades = actividadRepository.findByPat_IdOrderBySemanaAsc(id);

        // Admin: edita PATs generales (antes de aprobar) y de carrera
        // Coordinador: edita solo PATs de carrera (no generales)
        boolean puedeEditar = tieneRol(auth, "ROLE_ADMIN")
                || (tieneRol(auth, "ROLE_COORDINADOR") && !Boolean.TRUE.equals(pat.getEsGeneral()));

        // Tutor: solo puede AGREGAR actividades extra, no editar ni borrar
        boolean puedeAgregarSolo = tieneRol(auth, "ROLE_TUTOR") && !Boolean.TRUE.equals(pat.getEsGeneral())
                && "APROBADO".equals(pat.getEstado());

        boolean puedeAsignar = tieneRol(auth, "ROLE_ADMIN")
                && Boolean.TRUE.equals(pat.getEsGeneral())
                && "APROBADO".equals(pat.getEstado());

        model.addAttribute("pat", pat);
        model.addAttribute("actividades", actividades);
        model.addAttribute("puedeEditar", puedeEditar);
        model.addAttribute("puedeAgregarSolo", puedeAgregarSolo);
        model.addAttribute("puedeAsignar", puedeAsignar);
        return "pat/detalle";
    }

    // ── Eliminar ──
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        List<Actividad> actividades = actividadRepository.findByPat_IdOrderBySemanaAsc(id);

        // Orden correcto: asistencias → sesiones → actividades → PAT
        for (Actividad a : actividades) {
            List<com.example.tutorias.model.Sesion> sesiones = sesionRepository.findByActividad_Id(a.getId());
            for (com.example.tutorias.model.Sesion s : sesiones) {
                // Borrar asistencias de cada sesión antes de borrar la sesión
                asistenciaRepository.deleteAll(asistenciaRepository.findBySesion_Id(s.getId()));
            }
            sesionRepository.deleteAll(sesiones);
        }

        actividadRepository.deleteAll(actividades);
        patService.eliminar(id);
        flash.addFlashAttribute("mensaje", "PAT eliminado.");
        return "redirect:/pats";
    }

    // ── PDF ──
    @GetMapping("/pdf/{id}")
    public void generarPdfPat(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Pat pat = patRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PAT no encontrado"));
        List<Actividad> actividades = actividadRepository.findByPat_IdOrderBySemanaAsc(id);

        // Fix NullPointer: nombre dinámico según tipo de PAT
        String nombrePat = Boolean.TRUE.equals(pat.getEsGeneral())
                ? "General"
                : (pat.getCarrera() != null ? pat.getCarrera().getNombre() : "SinCarrera");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=PAT_" + nombrePat + ".pdf");

        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 40, 40);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        Font fTitulo = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(0x1e, 0x3a, 0x8a));
        Font fSub    = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(0x37, 0x41, 0x51));
        Font fHeader = new Font(Font.HELVETICA, 9,  Font.BOLD, Color.WHITE);
        Font fCelda  = new Font(Font.HELVETICA, 8,  Font.NORMAL, new Color(0x37, 0x41, 0x51));
        Font fBold   = new Font(Font.HELVETICA, 8,  Font.BOLD, new Color(0x1e, 0x3a, 0x8a));

        Paragraph inst = new Paragraph("Tecnológico Nacional de México — Campus Chilpancingo", fTitulo);
        inst.setAlignment(Element.ALIGN_CENTER);
        doc.add(inst);

        Paragraph depto = new Paragraph(
                "Departamento de Desarrollo Académico — Programa Institucional de Tutoría", fSub);
        depto.setAlignment(Element.ALIGN_CENTER);
        doc.add(depto);
        doc.add(new Paragraph(" "));

        PdfPTable lineaDiv = new PdfPTable(1);
        lineaDiv.setWidthPercentage(100);
        PdfPCell lineaCel = new PdfPCell();
        lineaCel.setBackgroundColor(new Color(0x1e, 0x3a, 0x8a));
        lineaCel.setFixedHeight(3);
        lineaCel.setBorder(Rectangle.NO_BORDER);
        lineaDiv.addCell(lineaCel);
        doc.add(lineaDiv);
        doc.add(new Paragraph(" "));

        Font fPatTitulo = new Font(Font.HELVETICA, 16, Font.BOLD, new Color(0x1e, 0x3a, 0x8a));
        String tipoPat = Boolean.TRUE.equals(pat.getEsGeneral()) ? "PAT General" : "Programa de Acción Tutorial (PAT)";
        Paragraph titulo = new Paragraph(tipoPat, fPatTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(4);
        doc.add(titulo);

        String carreraLabel = Boolean.TRUE.equals(pat.getEsGeneral())
                ? "GENERAL (aplica a todas las carreras)"
                : (pat.getCarrera() != null ? pat.getCarrera().getNombre() : "—");

        String infoPat = "Carrera: " + carreraLabel
                + "   |   Semestre: " + (pat.getSemestre() != null ? pat.getSemestre().getNombre() : "—")
                + "   |   Versión: " + (pat.getVersion() != null ? pat.getVersion() : "—")
                + "   |   Estado: " + (pat.getEstado() != null ? pat.getEstado() : "PENDIENTE");
        Paragraph info = new Paragraph(infoPat, fSub);
        info.setAlignment(Element.ALIGN_CENTER);
        info.setSpacingAfter(16);
        doc.add(info);

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{6, 9, 22, 20, 12, 11, 20});
        tabla.setSpacingBefore(10);

        Color azul = new Color(0x1e, 0x3a, 0x8a);
        String[] headers = {"Sem.", "Fecha", "Tema / Actividad", "Objetivo", "Tipo", "Ponente", "Recursos"};
        for (String h : headers) {
            PdfPCell cel = new PdfPCell(new Phrase(h, fHeader));
            cel.setBackgroundColor(azul);
            cel.setPadding(6);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tabla.addCell(cel);
        }

        boolean par = false;
        Color grisClaro = new Color(0xf8, 0xfa, 0xff);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");

        for (Actividad a : actividades) {
            Color fondo = par ? grisClaro : Color.WHITE;
            PdfPCell cSem = new PdfPCell(new Phrase(a.getSemana() != null ? "Sem " + a.getSemana() : "—", fBold));
            cSem.setBackgroundColor(new Color(0x4f, 0x46, 0xe5));
            cSem.setPadding(5);
            cSem.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cSem);
            agregar(tabla, a.getFecha() != null ? a.getFecha().format(fmt) : "—", fCelda, fondo);
            agregar(tabla, a.getTema() != null ? a.getTema() : "—", fCelda, fondo);
            agregar(tabla, a.getObjetivo() != null ? a.getObjetivo() : "—", fCelda, fondo);
            agregar(tabla, a.getTipo() != null ? a.getTipo() : "—", fCelda, fondo);
            agregar(tabla, a.getPonente() != null ? a.getPonente() : "—", fCelda, fondo);
            agregar(tabla, a.getRecursos() != null ? a.getRecursos() : "—", fCelda, fondo);
            par = !par;
        }

        if (actividades.isEmpty()) {
            PdfPCell vacio = new PdfPCell(new Phrase("Sin actividades registradas", fCelda));
            vacio.setColspan(7);
            vacio.setHorizontalAlignment(Element.ALIGN_CENTER);
            vacio.setPadding(10);
            tabla.addCell(vacio);
        }

        doc.add(tabla);
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph(" "));

        PdfPTable firmas = new PdfPTable(3);
        firmas.setWidthPercentage(80);
        firmas.setHorizontalAlignment(Element.ALIGN_CENTER);
        firmas.setSpacingBefore(40);
        String[] roles = {"Coordinador(a) de Tutoría", "Jefe(a) Académico(a)", "Subdirector(a) Académico(a)"};
        for (String rol : roles) {
            PdfPCell cf = new PdfPCell();
            cf.setBorder(Rectangle.NO_BORDER);
            cf.setPadding(10);
            cf.setHorizontalAlignment(Element.ALIGN_CENTER);
            Paragraph pf = new Paragraph();
            pf.add(new Chunk("________________________________\n", fSub));
            pf.add(new Chunk(rol, fSub));
            pf.setAlignment(Element.ALIGN_CENTER);
            cf.addElement(pf);
            firmas.addCell(cf);
        }
        doc.add(firmas);
        doc.close();
    }

    private void agregar(PdfPTable tabla, String texto, Font font, Color fondo) {
        PdfPCell cel = new PdfPCell(new Phrase(texto, font));
        cel.setBackgroundColor(fondo);
        cel.setPadding(5);
        cel.setVerticalAlignment(Element.ALIGN_TOP);
        tabla.addCell(cel);
    }

    private boolean tieneRol(Authentication auth, String rol) {
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> rol.equals(a.getAuthority()));
    }

    // ── Aprobar PAT General → lo marca como APROBADO ──
    @PostMapping("/aprobar/{id}")
    public String aprobar(@PathVariable Long id, RedirectAttributes flash) {
        patService.buscarPorId(id).ifPresent(p -> {
            p.setEstado("APROBADO");
            patRepository.save(p);
        });
        flash.addFlashAttribute("mensaje", "✅ PAT aprobado. Ya puede asignarse a carreras.");
        return "redirect:/pats/detalle/" + id;
    }

    // ── Rechazar PAT ──
    @PostMapping("/rechazar/{id}")
    public String rechazar(@PathVariable Long id, RedirectAttributes flash) {
        patService.buscarPorId(id).ifPresent(p -> {
            p.setEstado("RECHAZADO");
            patRepository.save(p);
        });
        flash.addFlashAttribute("mensaje", "❌ PAT rechazado. Puede editarse y volver a enviarse.");
        return "redirect:/pats/detalle/" + id;
    }

    // ── Reabrir (RECHAZADO → PENDIENTE) ──
    @PostMapping("/reabrir/{id}")
    public String reabrir(@PathVariable Long id, RedirectAttributes flash) {
        patService.buscarPorId(id).ifPresent(p -> {
            p.setEstado("PENDIENTE");
            patRepository.save(p);
        });
        flash.addFlashAttribute("mensaje", "PAT reabierto para edición.");
        return "redirect:/pats/detalle/" + id;
    }

}