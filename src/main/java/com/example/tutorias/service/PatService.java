package com.example.tutorias.service;

import com.example.tutorias.model.Actividad;
import com.example.tutorias.model.Pat;
import com.example.tutorias.repository.ActividadRepository;
import com.example.tutorias.repository.PatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatService {

    @Autowired private PatRepository patRepository;
    @Autowired private ActividadRepository actividadRepository;

    public List<Pat> findAll() { return patRepository.findAll(); }
    public List<Pat> listarTodos() { return patRepository.findAll(); }
    public Optional<Pat> buscarPorId(Long id) { return patRepository.findById(id); }
    public void eliminar(Long id) { patRepository.deleteById(id); }

    public Pat guardar(Pat pat) {
        boolean esNuevo = (pat.getId() == null);
        Pat guardado = patRepository.save(pat);
        if (esNuevo) {
            crearActividadesBase(guardado);
        }
        return guardado;
    }

    private void crearActividadesBase(Pat pat) {
        String[][] actividades = {
            {"1",  "Presentación y encuadre del programa de tutorías",    "INFORMACION",
             "Dar a conocer el programa institucional de tutorías y establecer acuerdos con el grupo.",
             "Reglamento interno, tríptico informativo del PIT"},
            {"2",  "Detección de necesidades académicas",                  "INFORMACION",
             "Identificar las necesidades académicas, personales y profesionales de los tutorados.",
             "Formato de detección de necesidades"},
            {"3",  "Hábitos y estrategias de estudio",                     "FORMACION",
             "Fortalecer técnicas y hábitos de estudio para mejorar el rendimiento académico.",
             "Guía de estrategias de estudio, material didáctico"},
            {"4",  "Administración del tiempo",                            "FORMACION",
             "Desarrollar habilidades de planificación y organización del tiempo escolar.",
             "Agenda escolar, formato de horario semanal"},
            {"5",  "Orientación vocacional y profesional",                 "ORIENTACION",
             "Orientar a los tutorados sobre su perfil profesional y campo laboral de su carrera.",
             "Material informativo de la carrera, plan de estudios"},
            {"6",  "Identificación de alumnos en riesgo académico",        "INFORMACION",
             "Detectar alumnos con materias reprobadas o bajo rendimiento y brindar orientación.",
             "Concentrado de calificaciones, instrumento de factores de reprobación"},
            {"7",  "Motivación y autoestima",                              "ORIENTACION",
             "Fomentar la confianza, motivación y actitud positiva ante los retos académicos.",
             "Dinámica grupal, material de apoyo psicopedagógico"},
            {"8",  "Trabajo en equipo y habilidades sociales",             "FORMACION",
             "Promover el trabajo colaborativo y el desarrollo de habilidades interpersonales.",
             "Actividad grupal, presentación"},
            {"9",  "Salud mental y manejo del estrés",                     "ORIENTACION",
             "Brindar herramientas para el manejo del estrés y el cuidado de la salud mental.",
             "Material de salud mental, contactos de orientación psicológica"},
            {"10", "Seguimiento académico — revisión de calificaciones",   "INFORMACION",
             "Revisar el avance académico de los tutorados e identificar áreas de mejora.",
             "Concentrado de calificaciones del periodo"},
            {"11", "Orientación para exámenes y evaluaciones",             "FORMACION",
             "Preparar a los tutorados para los periodos de evaluación con estrategias efectivas.",
             "Calendario de exámenes, guías de estudio"},
            {"12", "Proyecto de vida y metas personales",                  "ORIENTACION",
             "Reflexionar sobre metas personales, académicas y profesionales a corto y largo plazo.",
             "Formato de proyecto de vida"},
            {"13", "Cierre y evaluación del semestre tutorial",            "INFORMACION",
             "Evaluar el proceso tutorial del semestre y reconocer los logros alcanzados.",
             "Formato de evaluación del alumno a la tutoría grupal"}
        };

        // Calcular fecha de inicio — primer viernes del semestre activo
        // Si el PAT tiene semestre con fecha_inicio, usamos esa; si no, usamos hoy
        LocalDate fechaBase = (pat.getSemestre() != null && pat.getSemestre().getFechaInicio() != null)
                ? pat.getSemestre().getFechaInicio()
                : LocalDate.now();

        // Ajustar al primer viernes
        while (fechaBase.getDayOfWeek().getValue() != 5) { // 5 = FRIDAY
            fechaBase = fechaBase.plusDays(1);
        }

        for (String[] datos : actividades) {
            Actividad a = new Actividad();
            a.setSemana(Integer.parseInt(datos[0]));
            a.setTema(datos[1]);
            a.setTipo(datos[2]);
            a.setObjetivo(datos[3]);
            a.setRecursos(datos[4]);
            a.setPonente("Tutor asignado");
            // Cada semana es un viernes, 7 días después
            a.setFecha(fechaBase.plusWeeks(Integer.parseInt(datos[0]) - 1));
            a.setPat(pat);
            a.setActivo(true);
            actividadRepository.save(a);
        }
    }
}