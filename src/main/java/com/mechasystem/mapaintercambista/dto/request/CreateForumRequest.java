package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public record CreateForumRequest (
        @NotEmpty String username,
        @NotEmpty String titulo,
        @NotEmpty String comentario,
        @NotEmpty MultipartFile image
        ){
}
