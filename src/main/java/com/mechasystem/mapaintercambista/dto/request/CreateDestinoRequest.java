package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateDestinoRequest(
        @NotBlank String usernameAgencia,
        @NotBlank String idPais,
        @NotBlank BigDecimal preco,
        @NotBlank String cidade,
        @NotBlank String universidade,
        @NotBlank String descricao
        ) {
}
