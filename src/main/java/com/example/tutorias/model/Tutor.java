package com.example.tutorias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tutor",
       uniqueConstraints = @UniqueConstraint(name = "uk_tutor_usuario", columnNames = "id_usuario"))
public class Tutor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_carrera")
	private Carrera carrera;

	@Column(name = "activo", columnDefinition = "TINYINT(1)")
	private Boolean activo = true;

	@Column(name = "foto")
	private String foto;

	// Constructores
	public Tutor() {
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Carrera getCarrera() {
		return carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	// Método toString para depuración
	@Override
	public String toString() {
		return "Tutor [id=" + id + ", usuario=" + (usuario != null ? usuario.getId() : "null") + ", carrera="
				+ (carrera != null ? carrera.getId() : "null") + ", activo=" + activo + ", foto=" + foto + "]";
	}
}
