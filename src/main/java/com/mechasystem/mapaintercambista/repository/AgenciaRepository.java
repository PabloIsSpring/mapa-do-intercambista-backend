package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, UUID> {

    public Optional<Agencia> findAgenciaByUsername (String username);
}
