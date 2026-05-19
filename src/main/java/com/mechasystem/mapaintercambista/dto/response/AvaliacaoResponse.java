package com.mechasystem.mapaintercambista.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliacaoResponse(
        UUID id,
        DestinoResponse destino,
        IntercambistaResponse intercambistaResponse,
        String comentario,
        BigDecimal nota,
        LocalDateTime createdAt
) {
}
