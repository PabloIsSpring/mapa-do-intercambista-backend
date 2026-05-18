package com.mechasystem.mapaintercambista.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaResponse (
    UUID id,
    ForumResponse forum,
    IntercambistaResponse intercambistaResponse,
    String comentario,
    Integer likes,
    Integer dislikes,
    LocalDateTime createdAt
){
}
