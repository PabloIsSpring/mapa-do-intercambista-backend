package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateDestinoRequest(
        @NotBlank String usernameAgencia,
        @NotBlank UUID idPais,
        @NotBlank String cidade,
        @NotBlank String universidade,
        @NotBlank String descricao
        ) {
}
