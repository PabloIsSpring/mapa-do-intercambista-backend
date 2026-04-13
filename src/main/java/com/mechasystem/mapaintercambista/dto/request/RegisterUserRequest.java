package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(
        @NotEmpty(message = "Nome é obrigatório") String nome,
        @NotEmpty(message = "E-mail é obrigatório") @Email String email,
        @NotEmpty(message = "Senha é obrigatório") String password,
        @NotEmpty(message = "Username é obrigatório") String username,
        @NotEmpty(message = "Username é obrigatório") String sobrenome,
        @Min(0) int idade) {
}
