package com.example.tutorias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrera",
       uniqueConstraints = @UniqueConstraint(name = "uk_carrera_nombre", columnNames = "nombre"))
public class Carrera {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", nullable = false, length = 120)
	private String nombre;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}