package com.example.tutorias.service;

import com.example.tutorias.model.Usuario;
import com.example.tutorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

 
    public Usuario guardar(Usuario usuario) {
        // Verificar correo duplicado
        if (usuario.getId() == null) {
            // Usuario nuevo — verificar que el correo no exista
            if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
                throw new IllegalArgumentException(
                    "El correo '" + usuario.getCorreo() + "' ya está registrado.");
            }
        } else {
            // Edición — verificar que el correo no pertenezca a OTRO usuario
            usuarioRepository.findByCorreo(usuario.getCorreo())
                    .filter(u -> !u.getId().equals(usuario.getId()))
                    .ifPresent(u -> { throw new IllegalArgumentException(
                        "El correo '" + usuario.getCorreo() + "' ya está en uso."); });
        }

        if (usuario.getId() == null) {
            usuario.setContrasena(encriptarSiHaceFalta(usuario.getContrasena()));
        } else {
            Usuario existente = usuarioRepository.findById(usuario.getId()).orElse(null);
            if (existente != null) {
                String nueva = usuario.getContrasena();
                if (nueva == null || nueva.isBlank()) {
                    usuario.setContrasena(existente.getContrasena());
                } else {
                    usuario.setContrasena(encriptarSiHaceFalta(nueva));
                }
            }
        }
        return usuarioRepository.save(usuario);
    }

    private String encriptarSiHaceFalta(String pwd) {
        if (pwd == null) return null;
        if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$")) {
            return pwd;
        }
        return passwordEncoder.encode(pwd);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
