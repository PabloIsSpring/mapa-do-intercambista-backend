package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record PaisRequest (
        @NotBlank UUID id,
        @NotBlank String nome,
        @NotBlank String idiomaPrincipal,
        @NotBlank String moeda
        ){
}
