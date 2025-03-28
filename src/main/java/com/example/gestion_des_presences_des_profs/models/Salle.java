package com.example.gestion_des_presences_des_profs.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    public Salle(String libelleFieldText) {
        this.libelle=libelleFieldText;
    }
}
