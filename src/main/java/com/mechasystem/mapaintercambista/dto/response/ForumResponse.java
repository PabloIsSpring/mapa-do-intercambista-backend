package com.mechasystem.mapaintercambista.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ForumResponse(
        UUID id,
        IntercambistaResponse intercambista,
        String comentario,
        String titulo,
        String urlFotoDestino,
        Integer likes,
        Integer dislikes,
        LocalDateTime createdAt
) {
}
