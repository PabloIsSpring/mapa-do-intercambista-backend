package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.AgenciaUpdate;
import com.mechasystem.mapaintercambista.dto.request.CreateAgenciaRequest;
import com.mechasystem.mapaintercambista.dto.response.AgenciaResponse;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Agencia;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.AgenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    public AgenciaService (AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    public AgenciaResponse saveAgencia (CreateAgenciaRequest req, User u) {
        if(agenciaRepository.findAgenciaByCnpjOrRazaoSocialOrUsername(
                req.cnpj(),
                req.razaoSocial(),
                req.username()
        ).isPresent()){
            throw new ConflictException("Os dados já estão em uso");
        }

        Agencia a = new Agencia();
        a.setCnpj(req.cnpj());
        a.setUsername(req.username());
        a.setRazaoSocial(req.razaoSocial());
        a.setNomeFantasia(req.nomeFantasia());
        a.setUser(u);

        return mapperAgenciaResponse(agenciaRepository.save(a));
    }

    @Transactional
    public void deleteAgenciaByUsername(String username) {
        Agencia a = findAgenciaByUsername(username);

        a.getUser().setDeletedAt(LocalDate.now());

        agenciaRepository.save(a);
    }

    public AgenciaResponse updateAgenciaByUsername(AgenciaUpdate req) {
        if(agenciaRepository.findAgenciaByCnpjOrRazaoSocialOrUsername(
                req.cnpj(), req.razaoSocial(), req.newUsername()).isPresent()) {
            throw new ConflictException("Alguns dos atributos para update já estão em uso");
        }

        Agencia a = findAgenciaByUsername(req.username());

        a.setUsername(req.newUsername());
        a.setCnpj(req.cnpj());
        a.setRazaoSocial(req.razaoSocial());
        a.setNomeFantasia(req.nomeFantasia());

        return mapperAgenciaResponse(agenciaRepository.save(a));
    }

    public AgenciaResponse getAgenciaByUsername (String username) {
        Agencia a = findAgenciaByUsername(username);
        if (a.getDeletedAt() != null) {
            throw new NotFoundException("Essa agência não existe");
        }

        return mapperAgenciaResponse(a);
    }

    private Agencia findAgenciaByUsername(String username) {
        return agenciaRepository.findAgenciaByUsername(username)
                .orElseThrow(() -> new NotFoundException("Esse username de Agência não existe"));
    }

    private AgenciaResponse mapperAgenciaResponse (Agencia a) {
        return new AgenciaResponse(
                a.getUser().getEmail(),
                a.getNomeFantasia(),
                a.getCnpj(),
                a.getUsername(),
                a.getRazaoSocial()
        );
    }
}
