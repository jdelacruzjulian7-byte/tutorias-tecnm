	package com.example.tutorias.model;
	
	import jakarta.persistence.*;
	
	@Entity
	@Table(name = "asignacion", uniqueConstraints = @UniqueConstraint(name = "uk_asignacion_tutor_tutorado_semestre", columnNames = {
			"id_tutor", "id_tutorado", "id_semestre" }))
	public class Asignacion {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	
		@ManyToOne
		@JoinColumn(name = "id_tutor")
		private Tutor tutor;
	
		@ManyToOne
		@JoinColumn(name = "id_tutorado")
		private Tutorado tutorado;
	
		@ManyToOne
		@JoinColumn(name = "id_semestre")
		private Semestre semestre;
	
		@Column(name = "activo")
		private Boolean activo = true;
	
		public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
		public Tutor getTutor() {
			return tutor;
		}
	
		public void setTutor(Tutor tutor) {
			this.tutor = tutor;
		}
	
		public Tutorado getTutorado() {
			return tutorado;
		}
	
		public void setTutorado(Tutorado tutorado) {
			this.tutorado = tutorado;
		}
	
		public Semestre getSemestre() {
			return semestre;
		}
	
		public void setSemestre(Semestre semestre) {
			this.semestre = semestre;
		}
	
		public Boolean getActivo() {
			return activo;
		}
	
		public void setActivo(Boolean activo) {
			this.activo = activo;
		}
	
		@Override
		public String toString() {
			return "Asignacion [id=" + id + ", tutor=" + tutor + ", tutorado=" + tutorado + ", semestre=" + semestre
					+ ", activo=" + activo + "]";
		}
	
	}