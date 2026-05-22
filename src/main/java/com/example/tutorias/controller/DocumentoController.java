package com.example.tutorias.controller;

import com.example.tutorias.model.*;
import com.example.tutorias.repository.*;
import com.example.tutorias.service.AsistenciaService;
import com.example.tutorias.service.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Controller
@RequestMapping("/documentos")
public class DocumentoController {

	@Autowired
	private DocumentoService documentoService;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private SemestreRepository semestreRepository;
	@Autowired
	private TutoradoRepository tutoradoRepository;
	@Autowired
	private TutorRepository tutorRepository;
	@Autowired
	private AsignacionRepository asignacionRepository;
	@Autowired
	private AsistenciaService asistenciaService;
	@Autowired
	private DocumentoRepository documentoRepository;

	// ── CRUD ──
	@GetMapping
	public String listar(Model model) {
		model.addAttribute("documentos", documentoService.listarTodos());
		return "documento/listar";
	}

	@GetMapping("/nuevo")
	public String nuevo(Model model) {
	    model.addAttribute("documento", new Documento());
	    model.addAttribute("usuarios", usuarioRepository.findByRol_Nombre("TUTORADO"));
	    model.addAttribute("semestres", semestreRepository.findAll());
	    return "documento/formulario";
	}

	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Documento documento) {
		if (documento.getFechaEmision() == null)
			documento.setFechaEmision(LocalDate.now());
		documentoService.guardar(documento);
		return "redirect:/documentos";
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Long id, Model model) {
	    documentoService.buscarPorId(id).ifPresent(d -> model.addAttribute("documento", d));
	    model.addAttribute("usuarios", usuarioRepository.findByRol_Nombre("TUTORADO"));
	    model.addAttribute("semestres", semestreRepository.findAll());
	    return "documento/formulario";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
		documentoService.eliminar(id);
		return "redirect:/documentos";
	}

	// ── VISTAS PREVIAS DE DOCUMENTOS ──

	private void poblarModeloDocumento(Long usuarioId, Model model) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		Tutorado tutorado = tutoradoRepository.findByUsuario_Id(usuarioId).orElse(null);
		Semestre semestre = semestreRepository.findFirstByActivoTrue().orElse(null);

		Tutor tutor = null;
		double porcentaje = 0d;
		if (tutorado != null) {
			if (semestre != null) {
				tutor = asignacionRepository.findTutorByTutoradoAndSemestre(tutorado.getId(), semestre.getId())
						.orElse(null);
				porcentaje = asistenciaService.calcularPorcentaje(tutorado.getId(), semestre.getId());
			}
			// Si no hay tutor en el semestre activo, buscar la asignación más reciente
			if (tutor == null) {
				tutor = asignacionRepository.findAll().stream()
						.filter(a -> a.getTutorado() != null && a.getTutorado().getId().equals(tutorado.getId())
								&& Boolean.TRUE.equals(a.getActivo()) && a.getTutor() != null)
						.map(a -> a.getTutor())
						.findFirst().orElse(null);
			}
		}

		// Subdirector único
		Usuario subdirector = usuarioRepository.findAll().stream()
				.filter(u -> u.getRol() != null && "SUBDIRECTOR".equalsIgnoreCase(u.getRol().getNombre())).findFirst()
				.orElse(null);

		// Coordinador de la carrera del tutorado
		Usuario coordinador = null;
		if (tutorado != null && tutorado.getCarrera() != null) {
			coordinador = usuarioRepository.findAll().stream()
					.filter(u -> u.getRol() != null && "COORDINADOR".equalsIgnoreCase(u.getRol().getNombre()))
					.findFirst().orElse(null);
		}

		String fechaLarga = LocalDate.now()
				.format(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "MX")));

		model.addAttribute("usuario", usuario);
		model.addAttribute("tutorado", tutorado);
		model.addAttribute("semestre", semestre);
		model.addAttribute("tutor", tutor);
		model.addAttribute("subdirector", subdirector);
		model.addAttribute("coordinador", coordinador);
		model.addAttribute("porcentaje", porcentaje);
		model.addAttribute("apto", porcentaje >= 80d);
		model.addAttribute("fechaLarga", fechaLarga);
		model.addAttribute("fechaHoy", LocalDate.now());
	}

	@GetMapping("/generar/CARNET/{usuarioId}")
	public String verCarnet(@PathVariable Long usuarioId,
			@RequestParam(required = false) String origen,
			Model model) {
		poblarModeloDocumento(usuarioId, model);
		registrarDocumento(usuarioId, "CARNET");
		model.addAttribute("urlVolver", "tutorado".equals(origen) ? "/dashboard/tutorado" : "/documentos");
		return "documento/carnet";
	}

	@GetMapping("/generar/CONSTANCIA/{usuarioId}")
	public String verConstancia(@PathVariable Long usuarioId,
			@RequestParam(required = false) String origen,
			Model model,
	        org.springframework.security.core.Authentication auth) {
		try {
			boolean esPrivilegiado = auth != null && auth.getAuthorities().stream()
			    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUBDIRECTOR"));
			if (!esPrivilegiado) {
				Tutorado tutorado = tutoradoRepository.findByUsuario_Id(usuarioId).orElse(null);
				if (tutorado == null) {
					model.addAttribute("error", "El usuario no tiene perfil de tutorado registrado.");
					return "redirect:/documentos";
				}
				Semestre semestre = semestreRepository.findFirstByActivoTrue().orElse(null);
				double pct = (semestre != null) ? asistenciaService.calcularPorcentaje(tutorado.getId(), semestre.getId()) : 0d;
				if (pct < 80d) {
					model.addAttribute("error", "El tutorado no alcanza el 80% de asistencia requerido.");
					return "redirect:/documentos";
				}
			}
			poblarModeloDocumento(usuarioId, model);
			registrarDocumento(usuarioId, "CONSTANCIA");
			model.addAttribute("urlVolver", "tutorado".equals(origen) ? "/dashboard/tutorado" : "/documentos");
			return "documento/constancia";
		} catch (Exception e) {
			model.addAttribute("error", "Error al generar constancia: " + e.getMessage());
			return "redirect:/documentos";
		}
	}

	@GetMapping("/generar/OFICIO_TERMINO/{usuarioId}")
	public String verOficio(@PathVariable Long usuarioId, Model model,
	        org.springframework.security.core.Authentication auth) {
		poblarModeloDocumento(usuarioId, model);
		// Admin y Subdirector pueden ver sin restricción de porcentaje
		boolean esPrivilegiado = auth != null && auth.getAuthorities().stream()
		    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUBDIRECTOR"));
		if (!esPrivilegiado) {
			Object pctObj = model.getAttribute("porcentaje");
			double porcentaje = pctObj != null ? (double) pctObj : 0d;
			if (porcentaje < 80d) return "redirect:/dashboard/tutorado";
		}
		registrarDocumento(usuarioId, "OFICIO_TERMINO");
		return "documento/oficio_termino";
	}

	private void registrarDocumento(Long usuarioId, String tipo) {
		Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
		Semestre semestre = semestreRepository.findFirstByActivoTrue().orElse(null);
		if (usuario == null)
			return;
		boolean yaExiste = documentoRepository.findByUsuario_Id(usuarioId).stream()
				.anyMatch(d -> tipo.equals(d.getTipo()) && semestre != null && d.getSemestre() != null
						&& semestre.getId().equals(d.getSemestre().getId()));
		if (!yaExiste) {
			Documento doc = new Documento();
			doc.setUsuario(usuario);
			doc.setSemestre(semestre);
			doc.setTipo(tipo);
			doc.setFechaEmision(LocalDate.now());
			documentoRepository.save(doc);
		}
	}
}