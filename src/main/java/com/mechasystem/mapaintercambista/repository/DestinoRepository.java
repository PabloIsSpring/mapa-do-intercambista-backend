package com.mechasystem.mapaintercambista.repository;

import com.mechasystem.mapaintercambista.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, UUID> {

    public List<Destino> findDestinoByAgencia_Username(String username);

    public List<Destino> findDestinoByPais_Id(String idPais);

    public List<Destino> findDestinoByCidade(String cidade);


}
