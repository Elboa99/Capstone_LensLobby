package kennyboateng.Capstone_LensLobby.controllers;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.BadRequestException;
import kennyboateng.Capstone_LensLobby.payloads.FotografoLoginDTO;
import kennyboateng.Capstone_LensLobby.payloads.FotografoLoginResponseDTO;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;

import kennyboateng.Capstone_LensLobby.services.AuthorizationService;
import kennyboateng.Capstone_LensLobby.services.FotografoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private FotografoService fotografoService;

    // Endpoint per il login del fotografo
    @PostMapping("/login")
    public FotografoLoginResponseDTO login(@RequestBody @Validated FotografoLoginDTO body, BindingResult validationResult) throws Exception {
        if (validationResult.hasErrors()) {
            String errors = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new IllegalArgumentException("Errore di validazione: " + errors);
        }

        // Controlla le credenziali e genera il token
        Fotografo found = fotografoService.findByEmail(body.email())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato."));

        String token = authorizationService.checkCredentialsAndGenerateToken(body);
        return new FotografoLoginResponseDTO(token, found.getRuolo(), found.getId());
    }

    // Endpoint per registrare un nuovo fotografo
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public FotografoLoginResponseDTO register(@RequestParam("email") String email,
                                              @RequestParam("password") String password,
                                              @RequestParam("nome") String nome,
                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                              @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        FotografoPayloadDTO body = new FotografoPayloadDTO(email, password, nome);

        // Registra il fotografo
        Fotografo newFotografo = fotografoService.registerFotografo(body, profileImage, coverImage);

        // Genera il token JWT per il nuovo fotografo
        String token = authorizationService.generateToken(newFotografo);
        return new FotografoLoginResponseDTO(token, newFotografo.getRuolo(), newFotografo.getId());
    }
}