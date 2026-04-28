package com.mechasystem.mapaintercambista.dto.response;

import java.util.UUID;

public record PaisResponse(
        UUID id,
        String nome,
        String idiomaPrincipal,
        String moeda
) {
}
