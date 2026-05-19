package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "avaliacoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_intercambista", "id_destino"})
       }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_intercambista")
    private Intercambista intercambista;

    @ManyToOne
    @JoinColumn(name = "id_destino")
    private Destino destino;

    private String comentario;
    private BigDecimal nota;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
