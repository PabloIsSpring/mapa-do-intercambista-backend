package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Intercambista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IntercambistaRepository extends JpaRepository<Intercambista, UUID> {

    public Optional<Intercambista> findByUsername(String username);

}
