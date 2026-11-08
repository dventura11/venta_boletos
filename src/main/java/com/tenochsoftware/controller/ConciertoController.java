package com.tenochsoftware.controller;

import com.tenochsoftware.model.Concierto;
import com.tenochsoftware.repository.ConciertoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conciertos")
public class ConciertoController {

    private final ConciertoRepository repo;

    public ConciertoController(ConciertoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listarConciertos(Model model) {
        model.addAttribute("conciertos", repo.findAll());
        return "lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("concierto", new Concierto());
        return "formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Concierto concierto = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("concierto", concierto);
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarConcierto(@ModelAttribute Concierto concierto) {
        repo.save(concierto);
        return "redirect:/conciertos";
    }

    @getMapping("/eliminar/{id}")
    public String eliminarConcierto(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/conciertos";
    }

    @GetMapping("/comprar/{id}")
    public String mostrarPaginaCompra(@PathVariable Long id, Model model) {
        Concierto concierto = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("concierto", concierto);
        return "comprar";
    }

    @PostMapping("/comprar/procesar")
    public String procesarCompra(@RequestParam Long conciertoId, 
                                 @RequestParam String categoria, 
                                 @RequestParam Integer cantidad, 
                                 Model model) {
        Concierto concierto = repo.findById(conciertoId)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        if (concierto.getBoletosDisponibles() < cantidad) {
            model.addAttribute("error", "¡Lo sentimos! Solo quedan " + concierto.getBoletosDisponibles() + " boletos.");
            model.addAttribute("concierto", concierto);
            return "comprar";
        }

        concierto.setBoletosDisponibles(concierto.getBoletosDisponibles() - cantidad);
        repo.save(concierto);

        return "redirect:/conciertos";
    }
}
