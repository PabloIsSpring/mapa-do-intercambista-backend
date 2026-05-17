package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record CreateDestinoRequest(
        @NotBlank String usernameAgencia,
        @NotBlank String tipoDuracao,
        @NotBlank String tipoIntercambio,
        @NotBlank String idPais,
        @Positive int duracao,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotBlank String cidade,
        @NotBlank String universidade,
        @NotBlank String descricao,

        @NotNull
        MultipartFile image
        ) {
}
