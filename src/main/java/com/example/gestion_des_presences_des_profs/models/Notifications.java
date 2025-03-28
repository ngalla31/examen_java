package com.example.gestion_des_presences_des_profs.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Users destinataire;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    public Notifications(LocalDateTime dateEnvoi, String message, Users destinataire) {
        this.dateEnvoi = dateEnvoi;
        this.message = message;
        this.destinataire = destinataire;
    }
}

