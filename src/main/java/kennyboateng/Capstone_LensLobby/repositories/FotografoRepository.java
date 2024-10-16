package kennyboateng.Capstone_LensLobby.repositories;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotografoRepository extends JpaRepository<Fotografo, Long> {
}
