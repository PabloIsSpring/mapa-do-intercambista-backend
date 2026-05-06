package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.CreateDestinoRequest;
import com.mechasystem.mapaintercambista.dto.response.DestinoResponse;
import com.mechasystem.mapaintercambista.service.DestinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacotes")
public class DestinoController {

    private final DestinoService destinoService;

    public DestinoController (DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    @PostMapping
    public ResponseEntity<DestinoResponse> saveDestino(@RequestBody CreateDestinoRequest req) {
        return ResponseEntity.ok(destinoService.saveDestino(req));
    }

    @GetMapping
    public ResponseEntity<List<DestinoResponse>> getAllDestinos () {
        return ResponseEntity.ok(destinoService.getAllDestinos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinoResponse> getDestinoById(@PathVariable UUID id) {
        return ResponseEntity.ok(destinoService.getDestinoById(id));
    }

    @GetMapping("/{idPais}")
    public ResponseEntity<List<DestinoResponse>> getDestinosByPais(@PathVariable String idPais) {
        return ResponseEntity.ok(destinoService.getDestinosByPaisId(idPais));
    }

    @GetMapping("/{agenciaUsername}")
    public ResponseEntity<List<DestinoResponse>> getDestinoByAgencia(@PathVariable String agenciaUsername) {
        return ResponseEntity.ok(destinoService.getDestinoByAgenciaUsername(agenciaUsername));
    }

    @GetMapping("/{cidade}")
    public ResponseEntity<List<DestinoResponse>> getDestinoByCidade(@PathVariable String cidade) {
        return ResponseEntity.ok(destinoService.getDestinosByCidade(cidade));
    }
}
