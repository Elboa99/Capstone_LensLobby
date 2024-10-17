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
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "fotografo_id", nullable = false)
    private Fotografo fotografo;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

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