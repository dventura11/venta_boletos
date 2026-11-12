package com.tenochsoftware.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Concierto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Artista> artistas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "recinto_id")
    private Recinto recinto;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

    private Integer restriccionEdad;

    @ElementCollection
    private List<String> canalesVenta = new ArrayList<>();

    @OneToMany(mappedBy = "concierto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsientoConcierto> asientos = new ArrayList<>();

    public Concierto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Artista> getArtistas() {
        return artistas;
    }

    public void setArtistas(List<Artista> artistas) {
        this.artistas = artistas;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Integer getRestriccionEdad() {
        return restriccionEdad;
    }

    public void setRestriccionEdad(Integer restriccionEdad) {
        this.restriccionEdad = restriccionEdad;
    }

    public List<String> getCanalesVenta() {
        return canalesVenta;
    }

    public void setCanalesVenta(List<String> canalesVenta) {
        this.canalesVenta = canalesVenta;
    }

    public List<AsientoConcierto> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoConcierto> asientos) {
        this.asientos = asientos;
    }

    public int getTotalBoletosDisponibles() {
        int total = 0;
        for (AsientoConcierto a : asientos) {
            if (a.getBoletosDisponibles() != null) {
                total += a.getBoletosDisponibles();
            }
        }
        return total;
    }
}
