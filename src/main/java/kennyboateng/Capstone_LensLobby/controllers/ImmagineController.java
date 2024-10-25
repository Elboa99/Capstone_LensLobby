package kennyboateng.Capstone_LensLobby.controllers;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.enums.Categoria;
import kennyboateng.Capstone_LensLobby.payloads.ImmaginePayloadDTO;
import kennyboateng.Capstone_LensLobby.services.ImmagineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Immagine> getImmagineById(@PathVariable Long id) {
        Immagine immagine = immagineService.findImmagineById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Immagine non trovata con id: " + id));
        return ResponseEntity.ok(immagine);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createImmagine(@AuthenticationPrincipal Fotografo fotografo,
                                                 @RequestBody @Validated ImmaginePayloadDTO immaginePayload) {
        immagineService.salvaImmagineConUrl(immaginePayload, fotografo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Immagine creata con successo.");
    }



    @PostMapping("/{id}/upload")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> uploadImmagine(@PathVariable Long id, @RequestParam("fileImmagine") MultipartFile fileImmagine) throws IOException {
        immagineService.uploadImmagine(id, fileImmagine);
        return ResponseEntity.ok("Immagine caricata con successo.");
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteImmagine(@PathVariable Long id, @AuthenticationPrincipal Fotografo fotografo) {
        immagineService.deleteImmagine(id, fotografo.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/createWithFile")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createImmagineConFile(@AuthenticationPrincipal Fotografo fotografo,
                                                        @RequestParam("fileImmagine") MultipartFile fileImmagine,
                                                        @RequestParam("descrizione") String descrizione,
                                                        @RequestParam("categoria") String categoria) throws IOException {
        immagineService.salvaImmagineConFile(fotografo.getId(), fileImmagine, descrizione, categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body("Immagine caricata con successo.");
    }

}



