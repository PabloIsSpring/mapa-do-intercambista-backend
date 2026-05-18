package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RespostaRequest(
        @NotEmpty String username,
        @NotEmpty String comentario
) {
}
