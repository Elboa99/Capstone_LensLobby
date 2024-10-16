package kennyboateng.Capstone_LensLobby.services;

import kennyboateng.Capstone_LensLobby.entities.Categoria;
import kennyboateng.Capstone_LensLobby.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Optional<Categoria> findCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    public List<Categoria> findAllCategorie() {
        return categoriaRepository.findAll();
    }

    public Categoria saveCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }


    public void deleteCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}