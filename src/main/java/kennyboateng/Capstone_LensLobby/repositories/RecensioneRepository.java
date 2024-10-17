package kennyboateng.Capstone_LensLobby.repositories;

import kennyboateng.Capstone_LensLobby.entities.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    @Query("SELECT r FROM Recensione r WHERE r.fotografo.id = :fotografoId")
    List<Recensione> findRecensioniByFotografoId(@Param("fotografoId") Long fotografoId);

    List<Recensione> findByFotografoId(Long fotografoId);
}
