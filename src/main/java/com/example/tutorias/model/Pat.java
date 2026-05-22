package com.example.tutorias.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pat")
public class Pat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "version")
	private String version;

	@Column(name = "estado")
	private String estado;

	// TRUE = PAT General (sin carrera, creado por Admin/Desarrollo Académico)
	// FALSE = PAT de carrera (derivado del general, asignado a una carrera)
	@Column(name = "es_general", nullable = false)
	private Boolean esGeneral = false;

	@ManyToOne
	@JoinColumn(name = "id_semestre")
	private Semestre semestre;

	// Nullable: el PAT General no tiene carrera asignada
	@ManyToOne
	@JoinColumn(name = "id_carrera")
	private Carrera carrera;

	@Column(name = "activo")
	private Boolean activo = true;

	// ── Getters & Setters ──

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public Boolean getEsGeneral() { return esGeneral != null && esGeneral; }
	public void setEsGeneral(Boolean esGeneral) { this.esGeneral = esGeneral; }

	public Semestre getSemestre() { return semestre; }
	public void setSemestre(Semestre semestre) { this.semestre = semestre; }

	public Carrera getCarrera() { return carrera; }
	public void setCarrera(Carrera carrera) { this.carrera = carrera; }

	public Boolean getActivo() { return activo; }
	public void setActivo(Boolean activo) { this.activo = activo; }
}
