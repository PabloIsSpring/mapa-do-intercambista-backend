package com.mechasystem.mapaintercambista.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record IntercambistaUpdtRequest (@NotEmpty String username,
                                        @NotEmpty String nUsername){
}
