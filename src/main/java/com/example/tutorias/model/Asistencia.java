package com.example.tutorias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asistencia", uniqueConstraints = @UniqueConstraint(name = "uk_asistencia_tutorado_sesion", columnNames = {
		"id_tutorado", "id_sesion" }))
public class Asistencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "presente")
	private Boolean presente;

	@Column(name = "observacion", length = 500)
	private String observacion;

	@ManyToOne
	@JoinColumn(name = "id_tutorado")
	private Tutorado tutorado;

	@ManyToOne
	@JoinColumn(name = "id_sesion")
	private Sesion sesion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	public Tutorado getTutorado() {
		return tutorado;
	}

	public void setTutorado(Tutorado tutorado) {
		this.tutorado = tutorado;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}
	public String getObservacion() {
	    return observacion;
	}

	public void setObservacion(String observacion) {
	    this.observacion = observacion;
	}
}