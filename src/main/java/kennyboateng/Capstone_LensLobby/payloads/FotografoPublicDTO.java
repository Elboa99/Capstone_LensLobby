package kennyboateng.Capstone_LensLobby.payloads;

import java.util.List;

public record FotografoPublicDTO(
        Long id,
        String nome,
        String immagineProfilo,
        String copertina, List<ImmaginePayloadDTO> immagini
) {
}
