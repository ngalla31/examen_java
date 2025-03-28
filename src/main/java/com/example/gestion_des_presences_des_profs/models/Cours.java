package com.example.gestion_des_presences_des_profs.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "cours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clé étrangère vers l'utilisateur
    private Users user;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;
    @OneToMany(mappedBy = "cours")  // Cette annotation représente la relation avec Emargement
    private List<Emargements> emargements;  // Assure-toi que cet attribut est en minuscule

    public Cours(String nom, String description, LocalTime heureDebut, LocalTime heureFin, Salle salle) {
        this.nom = nom;
        this.description = description;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.salle = salle;
        this.user=user;
    }

    public Cours(String nom, String description, LocalTime heureDebut, LocalTime heureFin, Users user, Salle salle) {
        this.nom = nom;
        this.description = description;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.user = user;
        this.salle = salle;
    }

    // Getter pour la salle
    public Salle getSalle() {
        return salle;
    }

    // Getter pour le nom complet de l'utilisateur
    public String getNomCompletUser() {
        return user != null ? user.getPrenom() + " " + user.getNom() : "Aucun utilisateur";
    }


    // Vous pouvez ajouter un getter pour le libellé directement
    public String getLibelleSalle() {
        return salle != null ? salle.getLibelle() : "Aucune salle";
    }

    public LocalTime getHeureDebut() {
        return this.heureDebut;
    }

    public LocalTime getHeureFin() {
        return this.heureFin;
    }
}
