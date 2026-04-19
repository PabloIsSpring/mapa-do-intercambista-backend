package com.mechasystem.mapaintercambista.controller;


import com.mechasystem.mapaintercambista.dto.request.IntercambistaUpdtRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.service.IntercambistaService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/intercambista")
public class IntercambistaController {

    private final IntercambistaService intercambistaService;

    public IntercambistaController (IntercambistaService intercambistaService) {
        this.intercambistaService = intercambistaService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<IntercambistaResponse> getUserByUsername(@PathVariable @NotBlank String username) {
        return ResponseEntity.ok(intercambistaService.getIntecambistaByUsername(username));
    }
    @PutMapping
    public ResponseEntity<IntercambistaResponse> updateUsername (@RequestBody IntercambistaUpdtRequest request) {
        return ResponseEntity.ok(intercambistaService.updateUsername(request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<IntercambistaResponse> deleteByUsername (@PathVariable @NotBlank String username) {
            intercambistaService.deleteIntercambista(username);
            return ResponseEntity.noContent().build();
    }
}
