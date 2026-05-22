package com.example.tutorias.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "semestre",
       uniqueConstraints = @UniqueConstraint(
           name = "uk_semestre_periodo_anio",
           columnNames = {"periodo", "anio"}
       ))
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre generado automáticamente: "Ene-Jul 2025" o "Ago-Dic 2025" */
    @Column(name = "nombre")
    private String nombre;

    /** ENE_JUL  o  AGO_DIC */
    @Column(name = "periodo", length = 10)
    private String periodo;

    /** Año: 2024, 2025, 2026 … */
    @Column(name = "anio")
    private Integer anio;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo")
    private Boolean activo = false;

    // ─── getters / setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    /** Genera el nombre bonito a partir de periodo + año */
    public void generarNombre() {
        if (periodo != null && anio != null) {
            this.nombre = ("ENE_JUL".equals(periodo) ? "Ene-Jul " : "Ago-Dic ") + anio;
        }
    }
}