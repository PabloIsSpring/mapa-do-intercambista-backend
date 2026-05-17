package com.mechasystem.mapaintercambista.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DestinoResponse (
        UUID id,
        String cidade,
        String universidade,
        BigDecimal preco,
        String urlFoto,
        int duracao,
        String tipoPrograma,
        String tipoDuracao,
        AgenciaResponse agenciaResponse,
        PaisResponse paisResponse,
        String descricao,
        LocalDate deleteAt
) {
}
