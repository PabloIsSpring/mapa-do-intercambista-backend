package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="destinos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder

public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_agencia")
    private Agencia agencia;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais pais;

    private String cidade;
    private String universidade;
    private String descricao;

    @Builder.Default
    private int curtidas = 0;

    @Builder.Default
    @Column(name = "deleted_at")
    private LocalDate deletedAt = null;
}
