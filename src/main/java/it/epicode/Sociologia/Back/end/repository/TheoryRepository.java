package it.epicode.Sociologia.Back.end.repository;

import it.epicode.Sociologia.Back.end.model.Theory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TheoryRepository extends JpaRepository<Theory, Long> {
    Optional<Theory> findByNomeTeoria(String nomeTeoria);
    List<Theory> findByNomeTeoriaContainingIgnoreCase(String nomeTeoria);
    List<Theory> findByAutore(String autore);
    List<Theory> findByAutoreContainingIgnoreCase(String autore);

    Page<Theory> findByNomeTeoriaContainingIgnoreCaseOrAutoreContainingIgnoreCase(String nomeTeoriaKeyword, String autoreKeyword, Pageable pageable);

    @Query("SELECT t FROM Theory t WHERE " +
            "LOWER(t.nomeTeoria) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.autore) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.spiegazione) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.esempioApplicazioneModerna) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Theory> searchAllFields(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByNomeTeoria(String nomeTeoria);
}