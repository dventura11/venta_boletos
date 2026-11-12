package com.tenochsoftware.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AsientoConcierto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concierto_id")
    @JsonIgnore
    private Concierto concierto;

    @ManyToOne
    @JoinColumn(name = "tipo_asiento_id")
    private TipoAsiento tipoAsiento;

    private Double precio;
    private Integer boletosDisponibles;

    public AsientoConcierto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Concierto getConcierto() {
        return concierto;
    }

    public void setConcierto(Concierto concierto) {
        this.concierto = concierto;
    }

    public TipoAsiento getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(TipoAsiento tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getBoletosDisponibles() {
        return boletosDisponibles;
    }

    public void setBoletosDisponibles(Integer boletosDisponibles) {
        this.boletosDisponibles = boletosDisponibles;
    }
}
