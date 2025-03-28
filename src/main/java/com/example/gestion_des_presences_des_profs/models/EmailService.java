package com.example.gestion_des_presences_des_profs.models;
import com.example.gestion_des_presences_des_profs.dao.DBConnexion;
import com.example.gestion_des_presences_des_profs.dao.NotificationsImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Properties;

public class EmailService {
    NotificationsImpl notif=new NotificationsImpl();
    DBConnexion dbConnexion = new DBConnexion();
    EntityManager db = dbConnexion.getDb();
    private final String emailExpediteur = "oumarmbaye004@gmail.com";
    private final String motDePasse = "vyuy knwf bztb gtju"; // ⚠️ Utiliser une variable d'environnement

    public void envoyerEmail(String destinataire, String sujet, String messageTexte) {
        // Configuration SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Création de session authentifiée
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                System.out.println("Authentification : " + emailExpediteur); // Log de l'email pour vérifier
                return new PasswordAuthentication(emailExpediteur, motDePasse);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailExpediteur));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setText(messageTexte);

            // Envoi de l'email
            Transport.send(message);
            //System.out.println("📧 Email envoyé à : " + destinataire);
            // Affichage de l'alerte
            JOptionPane.showMessageDialog(null, "📧 Email envoyé à : " + destinataire, "Email Envoyé", JOptionPane.INFORMATION_MESSAGE);
            LocalDateTime dateEnvoi=LocalDateTime.now();
           /// enregistrerNotification(destinataire, String.valueOf(message),dateEnvoi);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur d’envoi d’email ! Message d'erreur : " + e.getMessage());
        }
    }






}
