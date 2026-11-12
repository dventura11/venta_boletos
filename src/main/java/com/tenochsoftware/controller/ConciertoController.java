package com.tenochsoftware.controller;

import com.tenochsoftware.model.Artista;
import com.tenochsoftware.model.AsientoConcierto;
import com.tenochsoftware.model.Concierto;
import com.tenochsoftware.model.Recinto;
import com.tenochsoftware.model.TipoAsiento;
import com.tenochsoftware.repository.ArtistaRepository;
import com.tenochsoftware.repository.AsientoConciertoRepository;
import com.tenochsoftware.repository.ConciertoRepository;
import com.tenochsoftware.repository.RecintoRepository;
import com.tenochsoftware.repository.TipoAsientoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/conciertos")
public class ConciertoController {

    private final ConciertoRepository repo;
    private final ArtistaRepository artistaRepo;
    private final RecintoRepository recintoRepo;
    private final TipoAsientoRepository tipoAsientoRepo;
    private final AsientoConciertoRepository asientoRepo;

    public ConciertoController(ConciertoRepository repo,
                               ArtistaRepository artistaRepo,
                               RecintoRepository recintoRepo,
                               TipoAsientoRepository tipoAsientoRepo,
                               AsientoConciertoRepository asientoRepo) {
        this.repo = repo;
        this.artistaRepo = artistaRepo;
        this.recintoRepo = recintoRepo;
        this.tipoAsientoRepo = tipoAsientoRepo;
        this.asientoRepo = asientoRepo;
    }

    private void cargarListas(Model model, Concierto concierto) {
        model.addAttribute("artistas", artistaRepo.findAll());
        model.addAttribute("recintos", recintoRepo.findAll());
        List<Long> idsSel = new ArrayList<>();
        if (concierto.getArtistas() != null) {
            for (Artista a : concierto.getArtistas()) {
                idsSel.add(a.getId());
            }
        }
        model.addAttribute("idsSel", idsSel);
        model.addAttribute("canalesDisponibles", Arrays.asList("En línea", "Efectivo", "Taquilla"));
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("conciertos", repo.findAll());
        return "lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Concierto concierto = new Concierto();
        concierto.setHora(LocalTime.of(19, 0));
        concierto.setCanalesVenta(new ArrayList<>(Arrays.asList("En línea", "Efectivo", "Taquilla")));
        concierto.setRestriccionEdad(3);
        model.addAttribute("concierto", concierto);
        cargarListas(model, concierto);
        return "formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Concierto concierto = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
        model.addAttribute("concierto", concierto);
        cargarListas(model, concierto);
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Concierto concierto,
                          @RequestParam(value = "artistaIds", required = false) List<Long> artistaIds,
                          Model model) {
        List<Artista> artistas = (artistaIds != null && !artistaIds.isEmpty())
                ? artistaRepo.findAllById(artistaIds) : new ArrayList<>();
        Recinto recinto = (concierto.getRecinto() != null && concierto.getRecinto().getId() != null)
                ? recintoRepo.findById(concierto.getRecinto().getId()).orElse(null) : null;

        concierto.setArtistas(artistas);
        concierto.setRecinto(recinto);

        String error = validar(concierto, artistas, recinto);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("concierto", concierto);
            cargarListas(model, concierto);
            return "formulario";
        }

        if (concierto.getAsientos() != null) {
            Iterator<AsientoConcierto> it = concierto.getAsientos().iterator();
            while (it.hasNext()) {
                AsientoConcierto asiento = it.next();
                if (asiento.getTipoAsiento() == null || asiento.getTipoAsiento().getId() == null) {
                    it.remove();
                    continue;
                }
                TipoAsiento tipo = tipoAsientoRepo.findById(asiento.getTipoAsiento().getId()).orElse(null);
                if (tipo == null) {
                    it.remove();
                    continue;
                }
                asiento.setTipoAsiento(tipo);
                asiento.setConcierto(concierto);
                if (asiento.getBoletosDisponibles() == null) {
                    asiento.setBoletosDisponibles(tipo.getCapacidad());
                }
                if (asiento.getPrecio() == null) {
                    asiento.setPrecio(tipo.getPrecioSugerido());
                }
            }
        }

        repo.save(concierto);
        return "redirect:/conciertos";
    }

    private String validar(Concierto concierto, List<Artista> artistas, Recinto recinto) {
        if (artistas == null || artistas.isEmpty()) {
            return "Debes seleccionar al menos un artista.";
        }
        if (recinto == null) {
            return "Debes seleccionar un recinto.";
        }
        LocalDate fecha = concierto.getFecha();
        if (fecha == null) {
            return "Debes indicar la fecha.";
        }
        if (concierto.getHora() == null) {
            return "Debes indicar la hora de inicio.";
        }
        if (!fecha.isAfter(LocalDate.now())) {
            return "La fecha debe ser futura.";
        }
        Long idActual = concierto.getId() != null ? concierto.getId() : -1L;
        if (repo.existeArtistaOcupado(artistas, fecha, idActual)) {
            return "Alguno de los artistas ya tiene un concierto esa fecha.";
        }
        if (repo.existsByRecintoAndFechaAndIdNot(recinto, fecha, idActual)) {
            return "El recinto " + recinto.getNombre() + " ya está ocupado esa fecha.";
        }
        return null;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/conciertos";
    }

    @GetMapping("/comprar/{id}")
    public String mostrarCompra(@PathVariable Long id, Model model) {
        model.addAttribute("concierto", cargarConcierto(id));
        return "comprar";
    }

    private Concierto cargarConcierto(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido:" + id));
    }

    @PostMapping("/comprar/procesar")
    public String procesarCompra(@RequestParam Long asientoId,
                                 @RequestParam Integer cantidad,
                                 Model model) {
        AsientoConcierto asiento = asientoRepo.findById(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento inválido"));

        if (cantidad == null || cantidad <= 0) {
            return resultado(model, asiento, "La cantidad debe ser mayor a cero.", false);
        }
        if (asiento.getBoletosDisponibles() < cantidad) {
            return resultado(model, asiento,
                    "¡Lo sentimos! Solo quedan " + asiento.getBoletosDisponibles()
                            + " boletos en " + asiento.getTipoAsiento().getNombre() + ".", false);
        }

        asiento.setBoletosDisponibles(asiento.getBoletosDisponibles() - cantidad);
        asientoRepo.save(asiento);
        return resultado(model, asiento,
                "Compra realizada: " + cantidad + " boleto(s) de "
                        + asiento.getTipoAsiento().getNombre() + ".", true);
    }

    @PostMapping("/cancelar/procesar")
    public String procesarCancelacion(@RequestParam Long asientoId,
                                      @RequestParam Integer cantidad,
                                      Model model) {
        AsientoConcierto asiento = asientoRepo.findById(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento inválido"));

        if (cantidad == null || cantidad <= 0) {
            return resultado(model, asiento, "La cantidad debe ser mayor a cero.", false);
        }
        int capacidad = asiento.getTipoAsiento().getCapacidad() != null
                ? asiento.getTipoAsiento().getCapacidad() : asiento.getBoletosDisponibles();
        if (asiento.getBoletosDisponibles() + cantidad > capacidad) {
            return resultado(model, asiento,
                    "No puedes cancelar más boletos de los vendidos (capacidad "
                            + capacidad + ").", false);
        }

        asiento.setBoletosDisponibles(asiento.getBoletosDisponibles() + cantidad);
        asientoRepo.save(asiento);
        return resultado(model, asiento,
                "Cancelación realizada: " + cantidad + " boleto(s) de "
                        + asiento.getTipoAsiento().getNombre() + " devueltos.", true);
    }

    private String resultado(Model model, AsientoConcierto asiento, String mensaje, boolean exito) {
        Concierto concierto = cargarConcierto(asiento.getConcierto().getId());
        model.addAttribute("concierto", concierto);
        model.addAttribute(exito ? "mensaje" : "error", mensaje);
        return "comprar";
    }
}
