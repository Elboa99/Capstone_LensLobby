package kennyboateng.Capstone_LensLobby.repositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import kennyboateng.Capstone_LensLobby.entities.Fotografo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


    @Repository
    public interface FotografoRepository extends JpaRepository<Fotografo, Long> {
        Optional<Fotografo> findByEmail(String email);

        @Query("SELECT f FROM Fotografo f LEFT JOIN FETCH f.immagini WHERE f.id = :id")
        Optional<Fotografo> findByIdWithImmagini(@Param("id") Long id);



        @Query("SELECT f FROM Fotografo f WHERE LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
        List<Fotografo> findByNomeContainingIgnoreCase(String nome);
    }





