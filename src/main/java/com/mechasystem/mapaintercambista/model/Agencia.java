package com.mechasystem.mapaintercambista.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="agencias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Agencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "agencia")
    private List<Destino> destinos;

    private String razaoSocial;
    private String nomeFantasia;
    private String username;
    private String cnpj;
}
