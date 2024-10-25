package kennyboateng.Capstone_LensLobby.services;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.enums.Categoria;
import kennyboateng.Capstone_LensLobby.exceptions.BadRequestException;
import kennyboateng.Capstone_LensLobby.exceptions.NotFoundException;
import kennyboateng.Capstone_LensLobby.payloads.ImmaginePayloadDTO;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ImmagineService {

    @Autowired
    private ImmagineRepository immagineRepository;

    @Autowired
    private FotografoRepository fotografoRepository;

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

    public Immagine salvaImmagineConUrl(ImmaginePayloadDTO immaginePayload, Long fotografoId) {
        Fotografo fotografo = fotografoRepository.findById(fotografoId)
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato"));

        Immagine immagine = new Immagine();
        immagine.setUrl(immaginePayload.url());
        immagine.setDescrizione(immaginePayload.descrizione());
        immagine.setCategoria(Categoria.valueOf(immaginePayload.categoria()));
        immagine.setFotografo(fotografo);

        return immagineRepository.save(immagine);
    }


    // Carica un'immagine su Cloudinary
    public Immagine uploadImmagine(Long immagineId, MultipartFile fileImmagine) throws IOException {
        Immagine immagine = immagineRepository.findById(immagineId)
                .orElseThrow(() -> new NotFoundException("Immagine non trovata"));

        if (fileImmagine == null || fileImmagine.isEmpty()) {
            throw new BadRequestException("File dell'immagine obbligatorio.");
        }

        Map risultatoUpload = cloudinary.uploader().upload(fileImmagine.getBytes(), ObjectUtils.asMap(
                "folder", "uploads_immagini",
                "public_id", "immagine_" + immagine.getFotografo().getId() + "_" + System.currentTimeMillis(),
                "transformation", ObjectUtils.asMap("width", 1920, "height", 1080, "crop", "limit")
        ));
        immagine.setUrl(risultatoUpload.get("url").toString());

        return immagineRepository.save(immagine);
    }

    public Immagine salvaImmagineConFile(Long fotografoId, MultipartFile fileImmagine, String descrizione, String categoria) throws IOException {
        Fotografo fotografo = fotografoRepository.findById(fotografoId)
                .orElseThrow(() -> new NotFoundException("Fotografo non trovato"));

        if (fileImmagine == null || fileImmagine.isEmpty()) {
            throw new BadRequestException("File dell'immagine obbligatorio.");
        }

        // Carica l'immagine su Cloudinary
        Map<String, Object> risultatoUpload = cloudinary.uploader().upload(fileImmagine.getBytes(), ObjectUtils.asMap(
                "folder", "uploads_immagini",
                "public_id", "immagine_" + fotografo.getId() + "_" + System.currentTimeMillis()
        ));
        String imageUrl = (String) risultatoUpload.get("url");

        // Crea una nuova entità Immagine
        Immagine immagine = new Immagine();
        immagine.setFotografo(fotografo);
        immagine.setUrl(imageUrl);
        immagine.setDescrizione(descrizione);
        immagine.setCategoria(Categoria.valueOf(categoria.toUpperCase()));

        // Salva l'immagine nel repository
        return immagineRepository.save(immagine);
    }




    // Elimina un'immagine
    @PreAuthorize("hasRole('ADMIN') or @immagineService.isImmagineOwner(#idImmagine, #fotografoId)")
    public void deleteImmagine(Long idImmagine, Long fotografoId) {
        Immagine immagine = immagineRepository.findById(idImmagine)
                .orElseThrow(() -> new NotFoundException("Immagine non trovata"));

        if (!immagine.getFotografo().getId().equals(fotografoId)) {
            throw new BadRequestException("Non autorizzato a eliminare questa immagine");
        }

        immagineRepository.delete(immagine);
    }

    // Metodo per verificare se l'utente è il proprietario dell'immagine
    public boolean isImmagineOwner(Long immagineId, Long fotografoId) {
        Immagine immagine = immagineRepository.findById(immagineId)
                .orElseThrow(() -> new NotFoundException("Immagine non trovata"));
        return immagine.getFotografo().getId().equals(fotografoId);
    }

    // Metodo per ottenere immagini casuali da tutti i fotografi
    public List<Immagine> findRandomImmagini(int count) {
        List<Immagine> allImmagini = immagineRepository.findAll();
        Random random = new Random();
        return allImmagini.stream()
                .sorted((a, b) -> random.nextInt(2) - 1)
                .limit(count)
                .toList();
    }
}
