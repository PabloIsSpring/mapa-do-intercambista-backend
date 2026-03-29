package com.mechasystem.mapaintercambista.config;

import lombok.Builder;

@Builder
public record JWTUserData(String userId, String email) {
}
