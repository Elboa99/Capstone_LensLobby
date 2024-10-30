package kennyboateng.Capstone_LensLobby.services;

import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.exceptions.NotFoundException;
import kennyboateng.Capstone_LensLobby.exceptions.UnauthorizedException;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPayloadDTO;
import kennyboateng.Capstone_LensLobby.payloads.FotografoPublicDTO;
import kennyboateng.Capstone_LensLobby.payloads.ImmaginePayloadDTO;
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



    public Fotografo findFotografoById(Long id) {
        return fotografoRepository.findByIdWithImmagini(id)
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato"));
    }



    // Metodo per ottenere tutti i fotografi
    public List<Fotografo> findAllFotografi() {
        return fotografoRepository.findAll();
    }

    public Fotografo registerFotografo(FotografoPayloadDTO body, MultipartFile profileImage, MultipartFile coverImage) throws IOException {
        // Controlla se esiste già un fotografo con la stessa email
        if (fotografoRepository.findByEmail(body.email()).isPresent()) {
            throw new IllegalStateException("Email già in uso");
        }

        // Mappiamo il DTO FotografoPayloadDTO a un'entità Fotografo
        Fotografo fotografo = new Fotografo();
        fotografo.setNome(body.nome());
        fotografo.setEmail(body.email());
        fotografo.setPassword(bcrypt.encode(body.password())); // Codifica la password

        // Gestione dell'immagine del profilo
        if (profileImage != null && !profileImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(profileImage.getBytes(), ObjectUtils.asMap(
                    "folder", "profile_images",
                    "public_id", "fotografo_" + fotografo.getEmail() + "_profile"
            ));
            fotografo.setImmagineProfilo(uploadResult.get("url").toString());
        }

        // Gestione dell'immagine di copertina
        if (coverImage != null && !coverImage.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(coverImage.getBytes(), ObjectUtils.asMap(
                    "folder", "cover_images",
                    "public_id", "fotografo_" + fotografo.getEmail() + "_cover"
            ));
            fotografo.setCopertina(uploadResult.get("url").toString());
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

    public FotografoPublicDTO findFotografoByIdPublic(Long id) {
        Fotografo fotografo = fotografoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato"));

        List<ImmaginePayloadDTO> immagini = fotografo.getImmagini().stream()
                .map(immagine -> new ImmaginePayloadDTO(
                        immagine.getUrl(),
                        immagine.getDescrizione(),
                        immagine.getCategoria().name()
                ))
                .toList();

        return new FotografoPublicDTO(
                fotografo.getId(),
                fotografo.getNome(),
                fotografo.getImmagineProfilo(),
                fotografo.getCopertina(),
                immagini
        );
    }

    public List<Fotografo> searchFotografiByNome(String nome) {
        return fotografoRepository.findByNomeContainingIgnoreCase(nome);
    }



}

