package kennyboateng.Capstone_LensLobby.controllers;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;
import kennyboateng.Capstone_LensLobby.services.FotografoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fotografi")
public class FotografoController {

    @Autowired
    private FotografoService fotografoService;

    // Endpoint per registrare un nuovo fotografo
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Fotografo registerFotografo(@Validated @RequestBody FotografoPayloadDTO body, BindingResult result,
                                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        if (result.hasErrors()) {
            throw new IllegalArgumentException("Errore di validazione nei dati del fotografo");
        }
        return fotografoService.registerFotografo(body, profileImage);
    }

    // Endpoint per aggiornare i dati del fotografo (ad esempio il proprio profilo)
    @PutMapping("/{id}")
    public Fotografo updateFotografo(@PathVariable Long id,
                                     @RequestBody FotografoPayloadDTO body,
                                     @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws Exception {
        Fotografo updatedFotografo = new Fotografo();
        updatedFotografo.setNome(body.nome());
        updatedFotografo.setEmail(body.email());



        return fotografoService.updateFotografo(id, updatedFotografo, profileImage);
    }

    // Endpoint per ottenere i dettagli di un fotografo in base all'ID
    @GetMapping("/{id}")
    public Fotografo getFotografoById(@PathVariable Long id) throws Exception {
        return fotografoService.findFotografoById(id);
    }
}
