package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record AgenciaUpdate(
        @NotEmpty String username,
        @NotEmpty String newUsername,
        @NotEmpty String nomeFantasia,
        @NotEmpty String razaoSocial,
        @NotEmpty String cnpj
        ) {
}
