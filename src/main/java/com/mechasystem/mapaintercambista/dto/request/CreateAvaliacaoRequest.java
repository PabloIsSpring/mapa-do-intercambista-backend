package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAvaliacaoRequest (
        @NotNull UUID idDestino,
        @NotEmpty String username,

        @NotEmpty String comentario,

        @NotNull
        @DecimalMax("5.0")
        @DecimalMin("0.0")
        BigDecimal nota
        ) {
}
