package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.IntercambistaUpdtRequest;
import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.IntercambistaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IntercambistaService {

    private final IntercambistaRepository intercambistaRepository;

    public IntercambistaService (IntercambistaRepository intercambistaRepository) {
        this.intercambistaRepository = intercambistaRepository;
    }

    private Intercambista findByUsername(String username) {
        return intercambistaRepository.findByUsername(username).
                orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com esse nome"));
    }

    private IntercambistaResponse mapperEntity(Intercambista i) {
        return new IntercambistaResponse(
                i.getUsername(),
                i.getNome(),
                i.getIdade()
        );
    }

    public IntercambistaResponse getIntecambistaByUsername (String username) {
        Intercambista u = findByUsername(username);

        return mapperEntity(u);
    }

    public IntercambistaResponse saveIntercambista(RegisterUserRequest request, User u) {
        if(intercambistaRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        Intercambista nInt = new Intercambista();
        nInt.setNome(request.nome());
        nInt.setSobrenome(request.sobrenome());
        nInt.setUser(u);
        nInt.setUsername(request.username());
        nInt.setIdade(request.idade());

        intercambistaRepository.save(nInt);

        return mapperEntity(nInt);
    }

    public IntercambistaResponse updateUsername(IntercambistaUpdtRequest u) {
        Intercambista nInt = findByUsername(u.username());
        if(intercambistaRepository.findByUsername(u.nUsername()).isPresent()) {
            throw new EntityExistsException("Username já está em uso");
        }

        nInt.setUsername(u.nUsername());

        intercambistaRepository.save(nInt);

        return mapperEntity(nInt);
    }

    @Transactional
    public void deleteIntercambista(String username) {
        Intercambista i = findByUsername(username);

        i.getUser().setDeletedAt(LocalDate.now());

        intercambistaRepository.save(i);
    }
}
