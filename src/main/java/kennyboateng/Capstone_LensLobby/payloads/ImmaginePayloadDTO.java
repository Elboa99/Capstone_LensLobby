package kennyboateng.Capstone_LensLobby.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ImmaginePayloadDTO(@NotEmpty(message = "L'URL dell'immagine è obbligatorio")
                                 String url,

                                 @NotNull(message = "L'ID del fotografo è obbligatorio")
                                 Long fotografoId,

                                 @NotNull(message = "L'ID della categoria è obbligatorio")
                                 Long categoriaId) {
}