package com.tenochsoftware.controller;

import com.tenochsoftware.model.Recinto;
import com.tenochsoftware.model.TipoAsiento;
import com.tenochsoftware.repository.RecintoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recintos")
public class RecintoController {

    private final RecintoRepository repo;

    public RecintoController(RecintoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("recintos", repo.findAll());
        return "recintos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("recinto", new Recinto());
        return "recintos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Recinto recinto = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("recinto", recinto);
        return "recintos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Recinto recinto) {
        if (recinto.getTiposAsiento() != null) {
            recinto.getTiposAsiento().removeIf(t -> t.getNombre() == null || t.getNombre().isBlank());
            for (TipoAsiento tipo : recinto.getTiposAsiento()) {
                tipo.setRecinto(recinto);
            }
        }
        repo.save(recinto);
        return "redirect:/recintos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/recintos";
    }
}
