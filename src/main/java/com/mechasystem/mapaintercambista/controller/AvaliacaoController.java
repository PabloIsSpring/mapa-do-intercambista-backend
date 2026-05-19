package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.CreateAvaliacaoRequest;
import com.mechasystem.mapaintercambista.dto.response.AvaliacaoResponse;
import com.mechasystem.mapaintercambista.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping("/avaliacoes")
    public ResponseEntity<AvaliacaoResponse> criarAvaliacao(@RequestBody @Valid CreateAvaliacaoRequest request) {
        AvaliacaoResponse response = avaliacaoService.salvarAvaliacao(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/destinos/{idDestino}/avaliacoes")
    public ResponseEntity<List<AvaliacaoResponse>> listarAvaliacoesPorDestino(@PathVariable UUID idDestino) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoesPorDestino(idDestino));
    }

    @GetMapping("/destinos/{idDestino}/avaliacoes/media")
    public ResponseEntity<Double> buscarMediaPorDestino(@PathVariable UUID idDestino) {
        return ResponseEntity.ok(avaliacaoService.buscarMediaPorDestino(idDestino));
    }

    @DeleteMapping("/avaliacoes/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable UUID id) {
        avaliacaoService.deletarAvaliacao(id);

        return ResponseEntity.noContent().build();
    }
}
