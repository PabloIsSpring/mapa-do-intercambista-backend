package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaisRequest (
        @NotBlank String id,
        @NotBlank String nome,
        @NotBlank String idiomaPrincipal,
        @NotBlank String moeda
        ){
}
