package kennyboateng.Capstone_LensLobby.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record FotografoLoginDTO(@NotEmpty(message = "L'email è obbligatoria")
                                @Email(message = "L'email non è valida")
                                String email,

                                @NotEmpty(message = "Inserisci una password")
                                @Size(min = 6, max = 40, message = "La password deve essere compresa tra 6 e 40 caratteri")
                                String password) {
}