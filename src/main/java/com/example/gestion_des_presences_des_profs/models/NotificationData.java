package com.example.gestion_des_presences_des_profs.models;

import java.time.LocalDateTime;

public class NotificationData {
    private String message;
    private String destinataire;
    private LocalDateTime dateEnvoi;

    public NotificationData(String message, String destinataire, LocalDateTime dateEnvoi) {
        this.message = message;
        this.destinataire = destinataire;
        this.dateEnvoi = dateEnvoi;
    }

    public String getMessage() {
        return message;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
}
