package com.mechasystem.mapaintercambista.dto.response;

public record AgenciaResponse(
        String email,
        String nomeFantasia,
        String cnpj,
        String username,
        String razaoSocial
) {
}
