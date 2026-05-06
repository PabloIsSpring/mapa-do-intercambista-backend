package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.CreateDestinoRequest;
import com.mechasystem.mapaintercambista.dto.response.DestinoResponse;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Agencia;
import com.mechasystem.mapaintercambista.model.Destino;
import com.mechasystem.mapaintercambista.model.Pais;
import com.mechasystem.mapaintercambista.repository.DestinoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DestinoService {

    private final DestinoRepository destinoRepository;
    private final PaisService paisService;
    private final AgenciaService agenciaService;

    public DestinoService (DestinoRepository destinoRepository, PaisService paisService,
                           AgenciaService agenciaService) {
        this.destinoRepository = destinoRepository;
        this.paisService = paisService;
        this.agenciaService = agenciaService;
    }

    public void curtirDestino (UUID id) {
        Destino d = findDestinoById(id);
        d.setCurtidas(d.getCurtidas() + 1);

        destinoRepository.save(d);
    }

    public void descurtirDestino(UUID id) {
        Destino d = findDestinoById(id);
        if(d.getCurtidas() >= 0) {
            d.setCurtidas(d.getCurtidas() - 1);
            destinoRepository.save(d);
        } else {

        }
    }

    public DestinoResponse saveDestino (CreateDestinoRequest req) {
        Agencia a = agenciaService.findAgenciaByUsername(req.usernameAgencia());
        Pais p = paisService.findPaisById(req.idPais());
        Destino d = new Destino();

        d.setAgencia(a);
        d.setPais(p);
        d.setCidade(req.cidade());
        d.setUniversidade(req.universidade());
        d.setDescricao(req.descricao());
        d.setPreco(req.preco());

        return mapperDestinoResponse(destinoRepository.save(d));
    }

    public List<DestinoResponse> getAllDestinos() {
        return mapperListDestinoResponse(
                destinoRepository.findAll()
        );
    }

    public List<DestinoResponse> getDestinosByCidade(String cidade) {
        return mapperListDestinoResponse(
                destinoRepository.findDestinoByCidade(cidade)
        );
    }

    public List<DestinoResponse> getDestinosByPaisId(String id) {
        return mapperListDestinoResponse(
                destinoRepository.findDestinoByPais_Id(id)
        );
    }

    public List<DestinoResponse> getDestinoByAgenciaUsername (String username) {
        return mapperListDestinoResponse(
                destinoRepository.findDestinoByAgencia_Username(username)
        );
    }

    public DestinoResponse getDestinoById(UUID id) {
        if(!destinoRepository.findById(id).isPresent()) {
            throw new NotFoundException("Destino não encontrado");
        }

        return mapperDestinoResponse(findDestinoById(id));
    }

    public Destino findDestinoById(UUID id) {
        return destinoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Destino não encontrado"));
    }

    public List<DestinoResponse> mapperListDestinoResponse(List<Destino> list) {
        if(list == null) {
            return List.of();
        }

        List<DestinoResponse> responses = new ArrayList<>();
        for(Destino destino: list){
            responses.add(mapperDestinoResponse(destino));
        }
        return responses;
    }

    public DestinoResponse mapperDestinoResponse (Destino d) {
        return new DestinoResponse(
                d.getId(),
                d.getCidade(),
                d.getUniversidade(),
                d.getPreco(),
                agenciaService.mapperAgenciaResponse(d.getAgencia()),
                paisService.mapperPaisResponse(d.getPais()),
                d.getDescricao()
        );
    }
}
