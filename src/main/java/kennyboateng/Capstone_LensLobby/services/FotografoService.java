package kennyboateng.Capstone_LensLobby.services;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class FotografoService {

    @Autowired
    private FotografoRepository fotografoRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    public Optional<Fotografo> findFotografoById(Long id) {
        return fotografoRepository.findById(id);
    }


    public Fotografo saveFotografo(Fotografo fotografo) {
        return fotografoRepository.save(fotografo);
    }

    public List<Fotografo> findAllFotografi() {
        return fotografoRepository.findAll();
    }

   
    public Fotografo loadFotografoById(Long id) throws UnauthorizedException {
        return findFotografoById(id).orElseThrow(() -> new UnauthorizedException("Fotografo non trovato."));
    }

    public Fotografo updateFotografo(Long id, Fotografo updatedFotografo) {
        return fotografoRepository.findById(id)
                .map(fotografo -> {
                    fotografo.setNomeUtente(updatedFotografo.getNomeUtente());
                    fotografo.setNome(updatedFotografo.getNome());
                    fotografo.setEmail(updatedFotografo.getEmail());
                    fotografo.setBiografia(updatedFotografo.getBiografia());

                    return fotografoRepository.save(fotografo);
                })
                .orElseThrow(() -> new RuntimeException("Fotografo non trovato con id: " + id));
    }


    public void deleteFotografo(Long id) {
        fotografoRepository.deleteById(id);
    }

    public Optional<Fotografo> findByEmail(String email) {
        return fotografoRepository.findByEmail(email);
    }

    public Fotografo registerFotografo(FotografoPayloadDTO fotografoDTO) {
        Fotografo newFotografo = new Fotografo();
        newFotografo.setNomeUtente(fotografoDTO.nomeUtente());
        newFotografo.setEmail(fotografoDTO.email());
        newFotografo.setNome(fotografoDTO.nome());
        newFotografo.setBiografia(fotografoDTO.biografia());

        newFotografo.setPassword(bcrypt.encode(fotografoDTO.password()));
        return fotografoRepository.save(newFotografo);
    }
}
