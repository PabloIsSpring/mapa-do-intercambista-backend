package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "foruns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Forum {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_intercambista")
    private Intercambista intercambista;

    private String titulo;
    private String comentario;

    @Column(name = "url_foto_forum")
    private String urlFotoForum;

    @Builder.Default
    private Integer likes = 0;

    @Builder.Default
    private Integer dislikes = 0;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;



}
