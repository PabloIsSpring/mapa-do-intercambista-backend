package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.RespostaRequest;
import com.mechasystem.mapaintercambista.dto.response.RespostaResponse;
import com.mechasystem.mapaintercambista.service.RespostaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RespostaController {

    private final RespostaService respostaService;

    public RespostaController (RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @PostMapping("/foruns/{idForum}/respostas")
    public ResponseEntity<RespostaResponse> criarResposta(@PathVariable UUID idForum,
                                                          @RequestBody @Valid RespostaRequest request) {
        RespostaResponse response = respostaService.salvarResposta(idForum, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    @GetMapping("/foruns/{idForum}/respostas")
    public ResponseEntity<List<RespostaResponse>> listarRespostasPorForum(@PathVariable UUID idForum) {
        return ResponseEntity.ok(respostaService.listarRespostasPorForum(idForum));
    }

    @GetMapping("/respostas/{id}")
    public ResponseEntity<RespostaResponse> buscarRespostaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(respostaService.buscarRespostaPorId(id));
    }

    @PutMapping("/respostas/{id}/like")
    public ResponseEntity<RespostaResponse> curtirResposta(@PathVariable UUID id) {
        return ResponseEntity.ok(respostaService.curtirResposta(id));
    }

    @PutMapping("/respostas/{id}/deslike")
    public ResponseEntity<RespostaResponse> descurtirResposta(@PathVariable UUID id) {
        return ResponseEntity.ok(respostaService.descurtirResposta(id));
    }

    @DeleteMapping("/respostas/{id}")
    public ResponseEntity<Void> deletarResposta(@PathVariable UUID id) {
        respostaService.deletarResposta(id);

        return ResponseEntity.noContent().build();
    }
}
