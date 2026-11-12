package com.tenochsoftware.controller;

import com.tenochsoftware.model.Artista;
import com.tenochsoftware.repository.ArtistaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/artistas")
public class ArtistaController {

    private final ArtistaRepository repo;

    public ArtistaController(ArtistaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("artistas", repo.findAll());
        return "artistas/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("artista", new Artista());
        return "artistas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Artista artista = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("artista", artista);
        return "artistas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Artista artista) {
        repo.save(artista);
        return "redirect:/artistas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/artistas";
    }
}
