package com.tenochsoftware.controller;

import com.tenochsoftware.model.Concierto;
import com.tenochsoftware.repository.ConciertoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conciertos")
public class ConciertoRestController {

    private final ConciertoRepository repo;

    public ConciertoRestController(ConciertoRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/buscar")
    public List<Concierto> buscar(@RequestParam(value = "q", defaultValue = "") String query) {
        return repo.buscarPorFiltros(query);
    }
}
