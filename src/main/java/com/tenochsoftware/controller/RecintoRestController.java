package com.tenochsoftware.controller;

import com.tenochsoftware.model.TipoAsiento;
import com.tenochsoftware.repository.RecintoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recintos")
public class RecintoRestController {

    private final RecintoRepository repo;

    public RecintoRestController(RecintoRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{id}/tipos")
    public List<TipoAsiento> tipos(@PathVariable Long id) {
        return repo.findById(id)
                .map(r -> (List<TipoAsiento>) r.getTiposAsiento())
                .orElse(Collections.emptyList());
    }
}
