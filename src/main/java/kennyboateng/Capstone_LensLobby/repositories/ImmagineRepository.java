package kennyboateng.Capstone_LensLobby.repositories;

import kennyboateng.Capstone_LensLobby.entities.Immagine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {

}

