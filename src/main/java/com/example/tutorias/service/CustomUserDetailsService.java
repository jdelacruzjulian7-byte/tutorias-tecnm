package com.example.tutorias.service;

import com.example.tutorias.model.Usuario;
import com.example.tutorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		// Buscamos usuario por correo
		Usuario usuario = usuarioRepository.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

		String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre().trim().toUpperCase() : "SIN_ROL";
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rolNombre));

		return new org.springframework.security.core.userdetails.User(usuario.getCorreo(), usuario.getContrasena(), // ←
																													// tu
																													// campo
																													// se
																													// llama
																													// contrasena,
																													// no
																													// password
				authorities);
	}
}