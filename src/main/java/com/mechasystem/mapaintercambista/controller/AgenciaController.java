package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.AgenciaUpdate;
import com.mechasystem.mapaintercambista.dto.response.AgenciaResponse;
import com.mechasystem.mapaintercambista.service.AgenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/agencia")
public class AgenciaController {

    private final AgenciaService agenciaService;

    public AgenciaController (AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<AgenciaResponse> getAgenciaByUsername(@PathVariable String username) {
        return ResponseEntity.ok(agenciaService.getAgenciaByUsername(username));
    }

    @PostMapping("{username}/foto")
    public ResponseEntity<AgenciaResponse> uploadFotoPerfil(@PathVariable String username,
                                                            @RequestParam("image") MultipartFile image) {
        return ResponseEntity.ok(agenciaService.uploadFotoPerfil(username, image));
    }

    @PutMapping
    public ResponseEntity<AgenciaResponse> updateAgenciaByUsername(@RequestBody AgenciaUpdate req) {
        return ResponseEntity.ok(agenciaService.updateAgenciaByUsername(req));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<AgenciaResponse> deleteAgenciaByUsername(@PathVariable String username) {
        agenciaService.deleteAgenciaByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
