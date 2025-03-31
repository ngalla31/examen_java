package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.models.NotificationData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationsImpl {
    public List<NotificationData> getNotifications() {
        DBConnexion dbConnexion = new DBConnexion();
        EntityManager db = dbConnexion.getDb();
        List<NotificationData> notifications = new ArrayList<>();

        try {
            db.getTransaction().begin();

            // Requête HQL pour récupérer le message, le nom du destinataire et la date d'envoi
            String hql = "SELECT n.message, u.nom, n.dateEnvoi "
                    + "FROM Notifications n "
                    + "JOIN n.destinataire u "
                    + "WHERE u.id = n.destinataire.id"; // Vous pouvez omettre cette condition si vous voulez toutes les notifications

            Query query = db.createQuery(hql);
            List<Object[]> resultList = query.getResultList();

            // Transformation des résultats en objets NotificationData
            for (Object[] result : resultList) {
                String message = (String) result[0]; // Message de la notification
                String destinataire = (String) result[1]; // Nom du destinataire
                LocalDateTime dateEnvoi = (LocalDateTime) result[2]; // Date d'envoi

                // Ajouter un objet NotificationData dans la liste
                notifications.add(new NotificationData(message, destinataire, dateEnvoi));
            }

            db.getTransaction().commit();
        } catch (RuntimeException e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e;
        }

        return notifications;
    }


}
