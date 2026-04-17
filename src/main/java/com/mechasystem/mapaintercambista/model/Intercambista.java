package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "intercambistas")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class Intercambista {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Column(unique = true  )
    private String username;

    private String nome;
    private String sobrenome;
    private int idade;

}
