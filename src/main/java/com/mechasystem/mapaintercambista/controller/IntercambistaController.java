package com.mechasystem.mapaintercambista.controller;


import com.mechasystem.mapaintercambista.dto.request.IntercambistaUpdtRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.service.IntercambistaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/intercambista")
public class IntercambistaController {

    private final IntercambistaService intercambistaService;

    public IntercambistaController (IntercambistaService intercambistaService) {
        this.intercambistaService = intercambistaService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<IntercambistaResponse> getUserByUsername(@PathVariable @NotNull String username) {
        try {
            return ResponseEntity.ok(intercambistaService.getIntecambistaByUsername(username));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe");
        }
    }

    @PutMapping
    public ResponseEntity<IntercambistaResponse> updateUsername (@RequestBody IntercambistaUpdtRequest request) {
        return ResponseEntity.ok(intercambistaService.updateUsername(request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<IntercambistaResponse> deleteByUsername (@PathVariable @NotNull String username) {
        try {
            intercambistaService.deleteIntercambista(username);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }
}
