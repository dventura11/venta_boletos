package com.tenochsoftware.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recinto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ciudad;

    @OneToMany(mappedBy = "recinto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TipoAsiento> tiposAsiento = new ArrayList<>();

    public Recinto() {
    }

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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<TipoAsiento> getTiposAsiento() {
        return tiposAsiento;
    }

    public void setTiposAsiento(List<TipoAsiento> tiposAsiento) {
        this.tiposAsiento = tiposAsiento;
    }
}
