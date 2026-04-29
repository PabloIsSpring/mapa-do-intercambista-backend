package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaisRepository extends JpaRepository<Pais, UUID> {

    public Optional<Pais> findPaisByNome(String nome);
}
