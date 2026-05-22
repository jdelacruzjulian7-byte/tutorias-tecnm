package com.example.tutorias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tutorado", uniqueConstraints = {
		@UniqueConstraint(name = "uk_tutorado_matricula", columnNames = "matricula"),
		@UniqueConstraint(name = "uk_tutorado_usuario", columnNames = "id_usuario") })
public class Tutorado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "matricula", nullable = false, length = 20)
	private String matricula;

	@Column(name = "semestre_ingreso")
	private String semestreIngreso;

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
	public Tutorado() {
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSemestreIngreso() {
		return semestreIngreso;
	}

	public void setSemestreIngreso(String semestreIngreso) {
		this.semestreIngreso = semestreIngreso;
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
		return "Tutorado [id=" + id + ", matricula=" + matricula + ", semestreIngreso=" + semestreIngreso + ", usuario="
				+ (usuario != null ? usuario.getId() : "null") + ", activo=" + activo + ", foto=" + foto + "]";
	}
}
