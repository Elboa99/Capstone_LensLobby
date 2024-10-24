package kennyboateng.Capstone_LensLobby.payloads;

import jakarta.validation.constraints.NotEmpty;


public record ImmaginePayloadDTO(
        @NotEmpty(message = "L'URL dell'immagine è obbligatorio")
        String url,

        String descrizione,

        @NotEmpty(message = "La categoria è obbligatoria")
        String categoria
) {
}

