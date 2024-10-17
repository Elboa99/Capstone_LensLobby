package kennyboateng.Capstone_LensLobby.controllers;

import jakarta.validation.Valid;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.services.FotografoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/fotografi")
public class FotografoController {

    @Autowired
    private FotografoService fotografoService;

    @GetMapping
    public List<Fotografo> getAllFotografi() {
        return fotografoService.findAllFotografi();
    }

    @GetMapping("/{id}")
    public Fotografo getFotografoById(@PathVariable Long id) {
        return fotografoService.findFotografoById(id)
                .orElseThrow(() -> new RuntimeException("Fotografo non trovato con id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fotografo createFotografo(@Valid @RequestBody Fotografo fotografo) {
        return fotografoService.saveFotografo(fotografo);
    }

    @PutMapping("/{id}")
    public Fotografo updateFotografo(@PathVariable Long id, @Valid @RequestBody Fotografo updatedFotografo) {
        return fotografoService.updateFotografo(id, updatedFotografo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFotografo(@PathVariable Long id) {
        fotografoService.deleteFotografo(id);
    }

    //*************** FILTRI *****************

    @GetMapping("/filter/nome")
    public List<Fotografo> filterByNome(@RequestParam String nome) {
        return fotografoService.findByNome(nome);
    }

    @GetMapping("/filter/username")
    public List<Fotografo> filterByUsername(@RequestParam String username) {
        return fotografoService.findByUsername(username);
    }
}
