package kennyboateng.Capstone_LensLobby.controllers;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.NotFoundException;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPublicDTO;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import kennyboateng.Capstone_LensLobby.services.FotografoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fotografi")

public class FotografoController {

    @Autowired
    private FotografoService fotografoService;

    @Autowired
    private FotografoRepository fotografoRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fotografo>> getAllFotografi() {
        List<Fotografo> fotografi = fotografoService.findAllFotografi();
        return ResponseEntity.ok(fotografi);
    }

    // Endpoint per registrare un nuovo fotografo
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Fotografo registerFotografo(
            @Validated @RequestBody FotografoPayloadDTO body,
            BindingResult result,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {

        if (result.hasErrors()) {
            throw new IllegalArgumentException("Errore di validazione nei dati del fotografo");
        }

        return fotografoService.registerFotografo(body, profileImage, coverImage);
    }

    // Endpoint per aggiornare i dati del fotografo (ad esempio il proprio profilo)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public Fotografo getFotografoById(@PathVariable Long id) throws Exception {
        return fotografoService.findFotografoById(id);
    }

    @GetMapping("/public/{id}")
    public FotografoPublicDTO getPublicFotografoById(@PathVariable Long id) throws NotFoundException {
        return fotografoService.findFotografoByIdPublic(id);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFotografo(@PathVariable Long id) throws Exception {
        fotografoService.deleteFotografo(id);
    }




    @GetMapping("/me")
    public Fotografo getProfile(@AuthenticationPrincipal Fotografo currentFotografo) {
        return fotografoRepository.findByIdWithImmagini(currentFotografo.getId())
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato"));
    }



    // Endpoint per aggiornare i dati del fotografo autenticato
    @PutMapping("/me")
    public Fotografo updateProfile(@AuthenticationPrincipal Fotografo currentFotografo,
                                   @RequestBody FotografoPayloadDTO body,
                                   @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws Exception {
        Fotografo updatedFotografo = new Fotografo();
        updatedFotografo.setNome(body.nome());
        updatedFotografo.setEmail(body.email());
        return fotografoService.updateFotografo(currentFotografo.getId(), updatedFotografo, profileImage);
    }

    // Endpoint per eliminare il profilo del fotografo autenticato
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Fotografo currentFotografo) throws Exception {
        fotografoService.deleteFotografo(currentFotografo.getId());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FotografoPublicDTO>> searchFotografi(@RequestParam String nome) {
        List<Fotografo> fotografi = fotografoService.searchFotografiByNome(nome);

        List<FotografoPublicDTO> risultati = fotografi.stream()
                .limit(5) // ✅ Limita a 5 risultati per non sovraccaricare il frontend
                .map(f -> new FotografoPublicDTO(f.getId(), f.getNome(), f.getImmagineProfilo(), f.getCopertina(), null))
                .toList();

        return ResponseEntity.ok(risultati);
    }


    // Endpoint per aggiornare l'immagine del profilo del fotografo autenticato
    @PatchMapping("/me/avatar")
    public Fotografo uploadAvatarPic(@AuthenticationPrincipal Fotografo currentFotografo,
                                     @RequestParam("profileImage") MultipartFile profileImage) throws Exception {
        return fotografoService.updateFotografo(currentFotografo.getId(), currentFotografo, profileImage);
    }

    @PatchMapping("/me/cover")
    public Fotografo uploadCoverPic(@AuthenticationPrincipal Fotografo currentFotografo,
                                    @RequestParam("coverImage") MultipartFile coverImage) throws Exception {
        return fotografoService.updateBackgroundPicture(currentFotografo.getId(), coverImage);
    }
}
