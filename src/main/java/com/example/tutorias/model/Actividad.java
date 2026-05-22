package com.example.tutorias.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "actividad")
public class Actividad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "semana")
	private Integer semana;

	@Column(name = "fecha")
	private LocalDate fecha;

	@Column(name = "tema")
	private String tema;

	@Column(name = "objetivo")
	private String objetivo;

	@Column(name = "ponente")
	private String ponente;

	@Column(name = "tipo") // INFORMACION, FORMACION, ORIENTACION
	private String tipo;

	@Column(name = "recursos")
	private String recursos;

	@ManyToOne
	@JoinColumn(name = "id_pat")
	private Pat pat;

	@Column(name = "activo")
	private Boolean activo = true;

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getPonente() {
		return ponente;
	}

	public void setPonente(String ponente) {
		this.ponente = ponente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getRecursos() {
		return recursos;
	}

	public void setRecursos(String recursos) {
		this.recursos = recursos;
	}

	public Pat getPat() {
		return pat;
	}

	public void setPat(Pat pat) {
		this.pat = pat;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}