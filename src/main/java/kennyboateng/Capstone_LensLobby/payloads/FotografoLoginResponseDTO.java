package kennyboateng.Capstone_LensLobby.payloads;

import kennyboateng.Capstone_LensLobby.enums.Role;

public record FotografoLoginResponseDTO(String accessToken, Role role, Long fotografoId) {
}

