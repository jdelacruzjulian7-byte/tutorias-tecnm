package com.example.tutorias.service;

import com.example.tutorias.model.Asistencia;
import com.example.tutorias.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public List<Asistencia> listarTodos() {
        return asistenciaRepository.findAll();
    }

    public Optional<Asistencia> buscarPorId(Long id) {
        return asistenciaRepository.findById(id);
    }

    public Asistencia guardar(Asistencia asistencia) {
        // upsert: si ya existe (tutorado, sesion) actualiza
        if (asistencia.getId() == null
            && asistencia.getTutorado() != null
            && asistencia.getSesion() != null) {
            asistenciaRepository.findByTutorado_IdAndSesion_Id(
                    asistencia.getTutorado().getId(),
                    asistencia.getSesion().getId())
                .ifPresent(existente -> asistencia.setId(existente.getId()));
        }
        return asistenciaRepository.save(asistencia);
    }

    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    public List<Asistencia> listarPorTutorado(Long idTutorado) {
        return asistenciaRepository.findByTutorado_Id(idTutorado);
    }

    public List<Asistencia> listarPorSesion(Long idSesion) {
        return asistenciaRepository.findBySesion_Id(idSesion);
    }

    /**
     * RF05 - Calcula el porcentaje de asistencia del tutorado en el semestre.
     * Devuelve 0.0 si el PAT del semestre aun no tiene sesiones registradas.
     */
    public double calcularPorcentaje(Long idTutorado, Long idSemestre) {
        if (idTutorado == null || idSemestre == null) return 0d;
        long total = 13L; // siempre 13 sesiones por PAT
        long presentes = asistenciaRepository
            .countPresentesPorTutoradoYSemestre(idTutorado, idSemestre);
        return Math.round((presentes * 10000.0d / total)) / 100.0d;
    }

    /** Atajo: ¿alcanza el 80% para constancia? (RF06) */
    public boolean cumple80(Long idTutorado, Long idSemestre) {
        return calcularPorcentaje(idTutorado, idSemestre) >= 80d;
    }
}
