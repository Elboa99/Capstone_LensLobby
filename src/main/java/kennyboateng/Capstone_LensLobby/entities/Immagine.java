package kennyboateng.Capstone_LensLobby.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "immagini")
public class Immagine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fotografo_id", nullable = false)
    private Fotografo fotografo;

    // Getters and setters
    // Dati EXIF
    @Column
    private String cameraMake;

    @Column
    private String cameraModel;

    @Column
    private String exposureTime;

    @Column
    private String aperture;

    @Column
    private String iso;

    @Column
    private String dateTimeOriginal;
}