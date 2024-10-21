package kennyboateng.Capstone_LensLobby.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import kennyboateng.Capstone_LensLobby.entities.Immagine;
import kennyboateng.Capstone_LensLobby.repositories.FotografoRepository;
import kennyboateng.Capstone_LensLobby.repositories.ImmagineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class ImmagineService {
    @Autowired
    private ImmagineRepository immagineRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private FotografoRepository fotografoRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Immagine> findAllImmagini() {
        return immagineRepository.findAll();
    }

    public Optional<Immagine> findImmagineById(Long id) {
        return immagineRepository.findById(id);
    }

    // Metodo per trovare tutte le immagini di un dato fotografo
    public List<Immagine> findImmaginiByFotografoId(Long fotografoId) {
        return immagineRepository.findByFotografoId(fotografoId);
    }

    // Metodo per trovare tutte le immagini di una data categoria
    public List<Immagine> findImmaginiByCategoriaId(Long categoriaId) {
        return immagineRepository.findByCategoriaId(categoriaId);
    }

    public Immagine saveImmagine(Immagine immagine) {
        return immagineRepository.save(immagine);
    }

    public Optional<Immagine> updateImmagine(Long id, Immagine updatedImmagine) throws Exception {
        return Optional.ofNullable(immagineRepository.findById(id).map(immagine -> {

            if (updatedImmagine.getUrl() != null) immagine.setUrl(updatedImmagine.getUrl());
            if (updatedImmagine.getCameraMake() != null) immagine.setCameraMake(updatedImmagine.getCameraMake());
            if (updatedImmagine.getCameraModel() != null) immagine.setCameraModel(updatedImmagine.getCameraModel());
            if (updatedImmagine.getDateTimeOriginal() != null)
                immagine.setDateTimeOriginal(updatedImmagine.getDateTimeOriginal());
            if (updatedImmagine.getExposureTime() != null) immagine.setExposureTime(updatedImmagine.getExposureTime());
            if (updatedImmagine.getAperture() != null) immagine.setAperture(updatedImmagine.getAperture());
            if (updatedImmagine.getIso() != null) immagine.setIso(updatedImmagine.getIso());


            if (updatedImmagine.getFotografo() != null && updatedImmagine.getFotografo().getId() != null) {
                Fotografo fotografo = fotografoRepository.findById(updatedImmagine.getFotografo().getId())
                        .orElseThrow(() -> new RuntimeException("Fotografo non trovato con id: " + updatedImmagine.getFotografo().getId()));
                immagine.setFotografo(fotografo);
            }
            if (updatedImmagine.getCategoria() != null && updatedImmagine.getCategoria().getId() != null) {
                Categoria categoria = categoriaRepository.findById(updatedImmagine.getCategoria().getId())
                        .orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + updatedImmagine.getCategoria().getId()));
                immagine.setCategoria(categoria);
            }

            return immagineRepository.save(immagine);
        }).orElseThrow(() -> new RuntimeException("Immagine non trovata con id: " + id)));
    }

    public Immagine saveImmagineWithExif(MultipartFile file, Long fotografoId, Long categoriaId) throws Exception {
        String url = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();

        Immagine immagine = new Immagine();
        immagine.setUrl(url);

        // Estrazione e salvataggio dei dati EXIF
        Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (directory != null) {
            immagine.setCameraMake(directory.getDescription(ExifIFD0Directory.TAG_MAKE));
            immagine.setCameraModel(directory.getDescription(ExifIFD0Directory.TAG_MODEL));
            immagine.setDateTimeOriginal(directory.getDescription(ExifIFD0Directory.TAG_DATETIME));

        }

        // Associa l'immagine a fotografo e categoria
        Fotografo fotografo = fotografoRepository.findById(fotografoId)
                .orElseThrow(() -> new RuntimeException("Fotografo non trovato con id: " + fotografoId));
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria non trovata con id: " + categoriaId));

        immagine.setFotografo(fotografo);
        immagine.setCategoria(categoria);

        return immagineRepository.save(immagine);
    }

        public void deleteImmagine(Long id) {
            immagineRepository.deleteById(id);
        }
    }

