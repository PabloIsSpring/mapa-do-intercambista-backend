package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RespostaRepository extends JpaRepository <Resposta, UUID> {

    public List<Resposta> findByForumIdAndDeletedAtIsNullOrderByCreateAtAsc(UUID idForum);

    public Optional<Resposta> findByIdAndDeletedAtIsNull(UUID id);
}
