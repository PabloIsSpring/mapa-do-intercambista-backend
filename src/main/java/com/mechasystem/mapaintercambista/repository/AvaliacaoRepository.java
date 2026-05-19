package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {

    public List<Avaliacao> findByDestinoIdAndDeletedAtIsNull(UUID idDestino);

    Optional<Avaliacao> findByIntercambistaUsernameAndDestinoIdAndDeletedAtIsNull(String username, UUID idDestino);

    Optional<Avaliacao> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Avaliacao> findByIntercambistaIdAndDestinoId(
            UUID idIntercambista,
            UUID idDestino
    );

    @Query("""
            SELECT AVG(a.nota)
            FROM Avaliacao a
            WHERE a.destino.id = :idDestino
            AND a.deletedAt IS NULL
            """)
    Double buscarMediaPorDestino(UUID idDestino);
}
