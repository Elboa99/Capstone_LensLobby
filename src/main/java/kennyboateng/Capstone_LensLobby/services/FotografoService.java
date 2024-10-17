package kennyboateng.Capstone_LensLobby.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import kennyboateng.Capstone_LensLobby.entities.Categoria;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.exceptions.UnauthorizedException;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;
import kennyboateng.Capstone_LensLobby.repositories.CategoriaRepository;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class FotografoService {

    @Autowired
    private FotografoRepository fotografoRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ImmagineRepository immagineRepository;

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

    public List<Fotografo> findByNome(String nome) {
        return fotografoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Fotografo> findByUsername(String username) {
        return fotografoRepository.findByNomeUtenteContainingIgnoreCase(username);
    }

}
