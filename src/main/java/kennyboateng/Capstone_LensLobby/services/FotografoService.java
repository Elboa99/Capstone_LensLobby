package kennyboateng.Capstone_LensLobby.services;

import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.NotFoundException;
import kennyboateng.Capstone_LensLobby.exceptions.UnauthorizedException;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FotografoService {

    @Autowired
    private FotografoRepository fotografoRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinary;

    // Metodo per trovare un fotografo per ID
    public Fotografo findFotografoById(Long id) throws NotFoundException {
        return fotografoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato con ID: " + id));
    }

    // Metodo per ottenere tutti i fotografi
    public List<Fotografo> findAllFotografi() {
        return fotografoRepository.findAll();
    }

    public Fotografo registerFotografo(FotografoPayloadDTO body, MultipartFile profileImage) throws IOException {
        // Mappiamo il DTO FotografoPayloadDTO a un'entità Fotografo
        Fotografo fotografo = new Fotografo();
        fotografo.setNome(body.nome());
        fotografo.setEmail(body.email());
        fotografo.setPassword(bcrypt.encode(body.password())); // Codifica la password

        // Gestione dell'immagine del profilo
        if (profileImage != null && !profileImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.asMap(
                    "folder", "profile_images",
                    "public_id", "fotografo_" + fotografo.getEmail()
            ));
            fotografo.setImmagineProfilo(uploadResult.get("url").toString());
        }

        return fotografoRepository.save(fotografo);
    }

    public Fotografo updateFotografo(Long id, Fotografo updatedFotografo, MultipartFile profileImage) throws Exception {
        Fotografo existingFotografo = fotografoRepository.findById(id)
                .orElseThrow(() -> new Exception("Fotografo not found"));

        existingFotografo.setNome(updatedFotografo.getNome());
        existingFotografo.setEmail(updatedFotografo.getEmail());

        if (profileImage != null && !profileImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.asMap(
                    "folder", "profile_images",
                    "public_id", "fotografo_" + updatedFotografo.getEmail()
            ));
            existingFotografo.setImmagineProfilo(uploadResult.get("url").toString());
        }

        return fotografoRepository.save(existingFotografo);
    }

    public void deleteFotografo(Long id) throws Exception {
        Fotografo fotografo = fotografoRepository.findById(id)
                .orElseThrow(() -> new Exception("Fotografo non trovato"));

        fotografoRepository.delete(fotografo);
    }

    // Metodo per trovare un fotografo per email
    public Optional<Fotografo> findByEmail(String email) {
        return fotografoRepository.findByEmail(email);
    }


}

