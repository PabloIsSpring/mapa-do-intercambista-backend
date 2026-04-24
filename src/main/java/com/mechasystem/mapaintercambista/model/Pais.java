package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="paises")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Pais {

    @Id
    private UUID id;

    private String nome;
    private String idioma;
    private String moeda;

    @Column(name = "deleted_at")
    @Builder.Default
    private LocalDate deletedAt = null;
}
