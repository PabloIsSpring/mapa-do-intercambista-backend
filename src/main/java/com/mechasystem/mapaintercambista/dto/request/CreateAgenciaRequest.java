package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record CreateAgenciaRequest(
        @NotEmpty(message = "e-mail é obrigatório") @Email String email,
        @NotEmpty(message = "senha é obrigatório") String password,
        @NotEmpty(message = "Razão Social é obrigatório") String razaoSocial,
        @NotEmpty(message = "Nome Fantasia é obrigatório") String nomeFantasia,
        @NotEmpty(message = "CNPJ é obrigatório") String cnpj,
        @NotEmpty(message = "Username é obrigatório") String username
) {

}
