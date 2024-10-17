package kennyboateng.Capstone_LensLobby.services;

import kennyboateng.Capstone_LensLobby.entities.Categoria;
import kennyboateng.Capstone_LensLobby.entities.Recensione;
import kennyboateng.Capstone_LensLobby.repositories.CategoriaRepository;
import kennyboateng.Capstone_LensLobby.repositories.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    public Recensione saveRecensione(Recensione recensione) {
        return recensioneRepository.save(recensione);
    }

    public List<Recensione> findAllRecensioni() {
        return recensioneRepository.findAll();
    }

    public Recensione createRecensione(Recensione recensione) {
        if (recensione.getId() != null) {
            throw new IllegalArgumentException("La recensione non deve avere un ID per essere creata");
        }
        return recensioneRepository.save(recensione);
    }

    public Optional<Recensione> findRecensioneById(Long id) {
        return recensioneRepository.findById(id);
    }

    public Optional<Recensione> updateRecensione(Long id, Recensione updatedRecensione) {
        return recensioneRepository.findById(id)
                .map(recensione -> {
                    recensione.setTesto(updatedRecensione.getTesto());
                    return recensioneRepository.save(recensione);
                });
    }

    public void deleteRecensione(Long id) {
        recensioneRepository.deleteById(id);
    }
}
