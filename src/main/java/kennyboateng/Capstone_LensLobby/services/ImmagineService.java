package kennyboateng.Capstone_LensLobby.services;

import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ImmagineService {
    @Autowired
    private ImmagineRepository immagineRepository;

    public Optional<Immagine> findImmagineById(Long id) {
        return immagineRepository.findById(id);
    }

    public Immagine saveImmagine(Immagine immagine) {
        return immagineRepository.save(immagine);
    }

    public List<Immagine> findAllImmagini() {
        return immagineRepository.findAll();
    }


    public void deleteImmagine(Long id) {
        immagineRepository.deleteById(id);
    }
}
