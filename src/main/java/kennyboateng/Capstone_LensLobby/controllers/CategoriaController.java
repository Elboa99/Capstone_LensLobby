package kennyboateng.Capstone_LensLobby.controllers;

import jakarta.validation.Valid;
import kennyboateng.Capstone_LensLobby.entities.Categoria;
import kennyboateng.Capstone_LensLobby.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorie")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping
    public List<Categoria> getAllCategorie() {
        return categoriaService.findAllCategorie();
    }


    @GetMapping("/{id}")
    public Categoria getCategoriaById(@PathVariable Long id) {
        return categoriaService.findCategoriaById(id)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + id));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria createCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaService.saveCategoria(categoria);
    }


    @PutMapping("/{id}")
    public Categoria updateCategoria(@PathVariable Long id, @Valid @RequestBody Categoria updatedCategoria) {
        return categoriaService.updateCategoria(id, updatedCategoria);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
    }
}