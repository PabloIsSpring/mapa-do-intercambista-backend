package com.mechasystem.mapaintercambista.dto.response;

import java.math.BigDecimal;

public record DestinoResponse (
        String id,
        String cidade,
        String universidade,
        BigDecimal preco,
        AgenciaResponse agenciaResponse,
        PaisResponse paisResponse,
        String descricao
) {
}
