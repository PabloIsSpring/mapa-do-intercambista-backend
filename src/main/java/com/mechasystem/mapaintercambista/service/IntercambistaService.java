package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.IntercambistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class IntercambistaService {

    private final IntercambistaRepository intercambistaRepository;

    public IntercambistaService (IntercambistaRepository intercambistaRepository) {
        this.intercambistaRepository = intercambistaRepository;
    }

    private Intercambista findByUsername(String username) {
        return intercambistaRepository.findByUsername(username).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Username não encontrado"));
    }

    public IntercambistaResponse saveIntercambista(RegisterUserRequest request, User u) {
        if(intercambistaRepository.findByUsername(request.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já está em uso");
        }

        Intercambista nInt = new Intercambista();
        nInt.setNome(request.nome());
        nInt.setSobrenome(request.sobrenome());
        nInt.setUser(u);
        nInt.setUsername(request.username());
        nInt.setIdade(request.idade());

        intercambistaRepository.save(nInt);

        return new IntercambistaResponse(
                nInt.getUsername(),
                nInt.getNome(),
                nInt.getIdade()
        );
    }

    public IntercambistaResponse updateUsername(String username, String nUsername) {
        Intercambista nInt = findByUsername(username);
        if(intercambistaRepository.findByUsername(nUsername).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já está em uso");
        }

        nInt.setUsername(nUsername);

        intercambistaRepository.save(nInt);

        return new IntercambistaResponse(
                nInt.getUsername(),
                nInt.getNome(),
                nInt.getIdade()
        );
    }
}
