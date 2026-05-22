package com.example.tutorias.service;

import com.example.tutorias.model.Carrera;
import com.example.tutorias.model.Tutorado;
import com.example.tutorias.model.Usuario;
import com.example.tutorias.repository.CarreraRepository;
import com.example.tutorias.repository.TutoradoRepository;
import com.example.tutorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutoradoService {

    @Autowired private TutoradoRepository tutoradoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CarreraRepository carreraRepository;

    public List<Tutorado> listarTodos() {
        return tutoradoRepository.findAll();
    }

    public Optional<Tutorado> buscarPorId(Long id) {
        return tutoradoRepository.findById(id);
    }

    public List<Tutorado> listarPorTutorYSemestre(Long idTutor, Long idSemestre) {
        return tutoradoRepository.findByTutorAndSemestre(idTutor, idSemestre);
    }

    public List<Tutorado> listarPorTutorCorreo(String correo) {
        return tutoradoRepository.findByTutorCorreo(correo);
    }

    public Tutorado guardar(Long usuarioId, Long carreraId, String matricula,
                            String semestreIngreso, boolean activo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));
        Carrera carrera = carreraRepository.findById(carreraId)
            .orElseThrow(() -> new RuntimeException("Carrera no encontrada: " + carreraId));

        Tutorado t = new Tutorado();
        t.setUsuario(usuario);
        t.setCarrera(carrera);
        t.setMatricula(matricula);
        t.setSemestreIngreso(semestreIngreso);
        t.setActivo(activo);
        return tutoradoRepository.save(t);
    }

    public Tutorado guardar(Tutorado tutorado) {
        return tutoradoRepository.save(tutorado);
    }

    public void eliminar(Long id) {
        tutoradoRepository.deleteById(id);
    }
}
