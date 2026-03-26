package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "E-mail é obrigatório") String email,
                           @NotEmpty(message = "Senha é obrigatório") String password) {
}
