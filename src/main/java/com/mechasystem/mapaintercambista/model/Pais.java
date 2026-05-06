package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
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
    private String id;

    private String nome;

    @Column(name="idioma_principal")
    private String idiomaPrincipal;
    private String moeda;

    @Column(name = "deleted_at")
    @Builder.Default
    private LocalDate deletedAt = null;

    @OneToMany(mappedBy = "pais")
    private List<Destino> destinos;
}
