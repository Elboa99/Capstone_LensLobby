package kennyboateng.Capstone_LensLobby.repositories;

import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FotografoRepository extends JpaRepository<Fotografo, Long> {

    Optional<Fotografo> findByEmail(String email);
    List<Fotografo> findByNomeContainingIgnoreCase(String nome);
    List<Fotografo> findByNomeUtenteContainingIgnoreCase(String nomeUtente);


}
