package kennyboateng.Capstone_LensLobby.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import kennyboateng.Capstone_LensLobby.enums.Categoria;
import kennyboateng.Capstone_LensLobby.enums.Role;
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

    @Enumerated(EnumType.STRING)
    private Categoria categoria ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fotografo_id", nullable = false)
    @JsonIgnoreProperties({"immagini", "hibernateLazyInitializer", "handler"})
    private Fotografo fotografo;



}