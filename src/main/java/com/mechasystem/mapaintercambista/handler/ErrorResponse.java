package com.mechasystem.mapaintercambista.handler;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String massage, String path, LocalDateTime localDateTime) {
}
