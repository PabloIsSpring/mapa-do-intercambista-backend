package com.mechasystem.mapaintercambista.dto.response;

public record PaisResponse(
        String id,
        String nome,
        String idiomaPrincipal,
        String moeda
) {
}
