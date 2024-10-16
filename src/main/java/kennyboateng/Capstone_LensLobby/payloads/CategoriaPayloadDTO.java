package kennyboateng.Capstone_LensLobby.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoriaPayloadDTO(@NotEmpty(message = "Il nome della categoria è obbligatorio")
                                  @Size(min = 2, max = 50, message = "Il nome della categoria deve essere compreso tra 2 e 50 caratteri")
                                  String nome) {
}

