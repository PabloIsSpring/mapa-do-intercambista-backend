package com.mechasystem.mapaintercambista.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record DestinoResponse (
        UUID id,
        String cidade,
        String universidade,
        BigDecimal preco,
        AgenciaResponse agenciaResponse,
        PaisResponse paisResponse,
        String descricao
) {
}
