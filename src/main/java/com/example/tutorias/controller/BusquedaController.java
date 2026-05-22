package com.example.tutorias.controller;

import com.example.tutorias.model.*;
import com.example.tutorias.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buscar")
public class BusquedaController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private PatRepository patRepository;
    @Autowired private AsignacionRepository asignacionRepository;
    @Autowired private SemestreRepository semestreRepository;
    @Autowired private CarreraRepository carreraRepository;

    @GetMapping
    public String formulario(Model model) {
        model.addAttribute("semestres", semestreRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        return "busqueda/buscar";
    }

    @GetMapping("/resultado")
    public String buscar(
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false) String tipoBusqueda,
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) Long semestreId,
            @RequestParam(required = false) Long carreraId,
            Model model) {

        model.addAttribute("semestres", semestreRepository.findAll());
        model.addAttribute("carreras", carreraRepository.findAll());
        model.addAttribute("entidad", entidad);
        model.addAttribute("tipoBusqueda", tipoBusqueda);
        model.addAttribute("termino", termino);
        model.addAttribute("semestreId", semestreId);
        model.addAttribute("carreraId", carreraId);

        if (entidad == null) return "busqueda/buscar";

        switch (entidad) {
            case "usuarios" -> {
                List<Usuario> resultados;
                if ("nombre".equals(tipoBusqueda) && termino != null && !termino.isBlank()) {
                    resultados = usuarioRepository
                            .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrCorreoContainingIgnoreCase(
                                    termino, termino, termino);
                } else {
                    resultados = usuarioRepository.findAll();
                }
                model.addAttribute("usuarios", resultados);
                model.addAttribute("total", resultados.size());
            }

            case "tutores" -> {
                List<Tutor> resultados;
                if ("nombre".equals(tipoBusqueda) && termino != null && !termino.isBlank()) {
                    resultados = tutorRepository
                            .findByUsuario_NombreContainingIgnoreCaseOrUsuario_ApellidoContainingIgnoreCase(
                                    termino, termino);
                } else if ("semestre".equals(tipoBusqueda) && semestreId != null) {
                    resultados = asignacionRepository.findBySemestre_Id(semestreId)
                            .stream().map(Asignacion::getTutor).distinct().toList();
                } else if ("carrera".equals(tipoBusqueda) && carreraId != null) {
                    resultados = tutorRepository.findByCarrera_Id(carreraId);
                } else {
                    resultados = tutorRepository.findAll();
                }
                model.addAttribute("tutores", resultados);
                model.addAttribute("total", resultados.size());
            }

            case "tutorados" -> {
                List<Tutorado> resultados;
                if ("nombre".equals(tipoBusqueda) && termino != null && !termino.isBlank()) {
                    resultados = tutoradoRepository
                            .findByUsuario_NombreContainingIgnoreCaseOrUsuario_ApellidoContainingIgnoreCase(
                                    termino, termino);
                } else if ("semestre".equals(tipoBusqueda) && semestreId != null) {
                    resultados = asignacionRepository.findBySemestre_Id(semestreId)
                            .stream().map(Asignacion::getTutorado).distinct().toList();
                } else if ("carrera".equals(tipoBusqueda) && carreraId != null) {
                    resultados = tutoradoRepository.findByCarrera_Id(carreraId);
                } else {
                    resultados = tutoradoRepository.findAll();
                }
                model.addAttribute("tutorados", resultados);
                model.addAttribute("total", resultados.size());
            }

            case "pats" -> {
                List<Pat> resultados;
                if ("semestre".equals(tipoBusqueda) && semestreId != null) {
                    resultados = patRepository.findBySemestre_Id(semestreId);
                } else if ("carrera".equals(tipoBusqueda) && carreraId != null) {
                    resultados = patRepository.findByCarrera_Id(carreraId);
                } else if ("nombre".equals(tipoBusqueda) && termino != null && !termino.isBlank()) {
                    resultados = patRepository.findByVersionContainingIgnoreCase(termino);
                } else {
                    resultados = patRepository.findAll();
                }
                model.addAttribute("pats", resultados);
                model.addAttribute("total", resultados.size());
            }

            case "asignaciones" -> {
                List<Asignacion> resultados;
                if ("semestre".equals(tipoBusqueda) && semestreId != null) {
                    resultados = asignacionRepository.findBySemestre_Id(semestreId);
                } else if ("nombre".equals(tipoBusqueda) && termino != null && !termino.isBlank()) {
                    resultados = asignacionRepository.findAll().stream()
                            .filter(a -> a.getTutor() != null && a.getTutor().getUsuario() != null
                                    && (a.getTutor().getUsuario().getNombre().toLowerCase()
                                            .contains(termino.toLowerCase())
                                    || a.getTutor().getUsuario().getApellido().toLowerCase()
                                            .contains(termino.toLowerCase())))
                            .toList();
                } else {
                    resultados = asignacionRepository.findAll();
                }
                model.addAttribute("asignaciones", resultados);
                model.addAttribute("total", resultados.size());
            }
        }

        return "busqueda/buscar";
    }
}