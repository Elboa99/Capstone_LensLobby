package kennyboateng.Capstone_LensLobby.controllers;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.services.ImmagineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/immagini")
public class ImmagineController {

    @Autowired
    private ImmagineService immagineService;


    @GetMapping
    public List<Immagine> getAllImmagini() {
        return immagineService.findAllImmagini();
    }


    @GetMapping("/{id}")
    public Immagine getImmagineById(@PathVariable Long id) {
        return immagineService.findImmagineById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Immagine non trovata con id: " + id));
    }




    @PutMapping("/{id}")
    public Immagine updateImmagine(@PathVariable Long id,
                                   @RequestBody Immagine immagine,
                                   @RequestParam(value = "newImageFile", required = false) MultipartFile newImageFile,
                                   @RequestParam(value = "fotografo") Fotografo fotografo) throws Exception {
        return immagineService.updateImmagine(id, immagine, newImageFile, fotografo);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImmagine(@PathVariable Long id, @RequestParam(value = "fotografo") Fotografo fotografo) throws Exception {
        immagineService.deleteImmagine(id, fotografo);
    }

}