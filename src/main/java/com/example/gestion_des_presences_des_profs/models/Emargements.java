package com.example.gestion_des_presences_des_profs.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emargements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Emargements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "professeur_id", nullable = false)
    private Users professeur;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

    public Emargements(LocalDate date, Users professeur, Cours cours) {
        this.date = date;
        this.professeur = professeur;
        this.cours = cours;
    }
}
