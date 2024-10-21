package kennyboateng.Capstone_LensLobby.services;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImmagineService {

    @Autowired
    private ImmagineRepository immagineRepository;

    @Autowired
    private Cloudinary cloudinary;

    // Metodo per trovare tutte le immagini
    public List<Immagine> findAllImmagini() {
        return immagineRepository.findAll();
    }

    // Metodo per trovare un'immagine per ID
    public Optional<Immagine> findImmagineById(Long id) {
        return immagineRepository.findById(id);
    }

    // Salva una nuova immagine
    public Immagine salvaImmagine(Immagine immagine, MultipartFile fileImmagine, Fotografo fotografo) throws IOException {
        if (fileImmagine != null && !fileImmagine.isEmpty()) {
            Map risultatoUpload = cloudinary.uploader().upload(fileImmagine.getBytes(), ObjectUtils.asMap(
                    "folder", "uploads_immagini",
                    "public_id", "immagine_" + fotografo.getId() + "_" + System.currentTimeMillis()
            ));
            immagine.setUrl(risultatoUpload.get("url").toString());
        }

        immagine.setFotografo(fotografo);
        return immagineRepository.save(immagine);
    }

    // Aggiorna un'immagine esistente
    public Immagine updateImmagine(Long idImmagine, Immagine immagineAggiornata, MultipartFile fileImmagine, Fotografo fotografo) throws Exception {
        Immagine immagineEsistente = immagineRepository.findById(idImmagine)
                .orElseThrow(() -> new Exception("Immagine non trovata"));

        if (!immagineEsistente.getFotografo().getId().equals(fotografo.getId())) {
            throw new Exception("Non autorizzato a modificare questa immagine");
        }

        if (fileImmagine != null && !fileImmagine.isEmpty()) {
            Map risultatoUpload = cloudinary.uploader().upload(fileImmagine.getBytes(), ObjectUtils.asMap(
                    "folder", "uploads_immagini",
                    "public_id", "immagine_" + fotografo.getId() + "_" + System.currentTimeMillis()
            ));
            immagineEsistente.setUrl(risultatoUpload.get("url").toString());
        }

        immagineEsistente.setDescrizione(immagineAggiornata.getDescrizione());
        return immagineRepository.save(immagineEsistente);
    }

    // Elimina un'immagine
    public void deleteImmagine(Long idImmagine, Fotografo fotografo) throws Exception {
        Immagine immagine = immagineRepository.findById(idImmagine)
                .orElseThrow(() -> new Exception("Immagine non trovata"));

        if (!immagine.getFotografo().getId().equals(fotografo.getId())) {
            throw new Exception("Non autorizzato a eliminare questa immagine");
        }

        immagineRepository.delete(immagine);
    }
}





