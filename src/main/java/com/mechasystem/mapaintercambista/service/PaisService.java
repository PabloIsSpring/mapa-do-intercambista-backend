package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.PaisRequest;
import com.mechasystem.mapaintercambista.dto.response.PaisResponse;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Pais;
import com.mechasystem.mapaintercambista.repository.PaisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PaisService {

    private final PaisRepository paisRepository;

    public PaisService (PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    public PaisResponse savePais (PaisRequest req) {
        if(paisRepository.findById(req.id()).isPresent() || paisRepository.findPaisByNome(req.nome()).isPresent()) {
            throw new ConflictException("Já existe algum pais com esse nome ou ID");
        }

        Pais p = new Pais();

        p.setId(req.id());
        p.setNome(req.nome());
        p.setMoeda(req.moeda());
        p.setIdiomaPrincipal(req.idiomaPrincipal());

        return mapperPaisResponse(paisRepository.save(p));
    }

    public PaisResponse getPaisById (String id) {
        Pais p = findPaisById(id);
        if(p.getDeletedAt() != null) {
            throw new NotFoundException("Esse país não existe");
        }

        return mapperPaisResponse(p);
    }

    public PaisResponse getPaisByNome (String nome) {
        Pais p = findPaisByNome(nome);
        if(p.getDeletedAt() != null) {
            throw new NotFoundException("Esse país não existe");
        }

        return mapperPaisResponse(p);
    }

    public void deletePaisById (String id) {
        Pais p = findPaisById(id);

        p.setDeletedAt(LocalDate.now());

        paisRepository.save(p);
    }

    public PaisResponse mapperPaisResponse (Pais p) {
        return new PaisResponse(
                p.getId(),
                p.getNome(),
                p.getIdiomaPrincipal(),
                p.getMoeda()
        );
    }

    public Pais findPaisById (String id) {
        return paisRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Esse id: "+ id +" não existe"));
    }

    private Pais findPaisByNome (String nome) {
        return paisRepository.findPaisByNome(nome)
                .orElseThrow(() -> new NotFoundException("Pais: "+nome+" não foi encontrado. Está bem pontuada?"));
    }

}
