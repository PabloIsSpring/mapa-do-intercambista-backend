package com.mechasystem.mapaintercambista.dto.response;

import java.util.UUID;

public record DestinoResponse (
        UUID id,
        String cidade,
        String universidade,
        AgenciaResponse agenciaResponse,
        PaisResponse paisResponse,
        String descricao
) {
}
