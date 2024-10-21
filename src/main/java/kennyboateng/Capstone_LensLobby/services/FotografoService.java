package kennyboateng.Capstone_LensLobby.services;

import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
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
import java.util.Map;

@Service
public class FotografoService {

    @Autowired
    private FotografoRepository fotografoRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary cloudinary;

    public Fotografo registerFotografo(Fotografo fotografo, MultipartFile profileImage) throws IOException {
        fotografo.setPassword(bcrypt.encode(fotografo.getPassword()));

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
}

