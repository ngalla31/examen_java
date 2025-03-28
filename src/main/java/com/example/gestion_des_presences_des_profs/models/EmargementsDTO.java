package com.example.gestion_des_presences_des_profs.models;

import com.example.gestion_des_presences_des_profs.dao.DBConnexion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EmargementsDTO {
    private SimpleIntegerProperty id;
    private SimpleStringProperty cours;
    private SimpleObjectProperty<Date> date;
    private SimpleStringProperty nomCompletUser;

    // ✅ Constructeur correct
    public EmargementsDTO(Integer id, String coursNom, Date date, String nomCompletUser) {
        this.id = new SimpleIntegerProperty(id);
        this.cours = new SimpleStringProperty(coursNom);
        this.date = new SimpleObjectProperty<>(date);
        this.nomCompletUser = new SimpleStringProperty(nomCompletUser);
    }

    public EmargementsDTO() {
    }

    // ✅ Getters
    public int getId() {
        return id.get();
    }

    public String getCours() {
        return cours.get();
    }

    public Date getDate() {
        return date.get();
    }

    public String getNomCompletUser() {
        return nomCompletUser.get();
    }

    // ✅ Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setCours(String cours) {
        this.cours.set(cours);
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public void setNomCompletUser(String nomCompletUser) {
        this.nomCompletUser.set(nomCompletUser);
    }

    // ✅ Méthodes JavaFX pour accéder aux propriétés
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty coursProperty() {
        return cours;
    }

    public SimpleObjectProperty<Date> dateProperty() {
        return date;
    }

    public SimpleStringProperty nomCompletUserProperty() {
        return nomCompletUser;
    }

    // ✅ Méthode pour charger les émargements
    public static ObservableList<EmargementsDTO> chargerEmargements() {
        // Connexion à la base de données
        DBConnexion dbConnexion = new DBConnexion();
        EntityManager db = dbConnexion.getDb();
        db.getTransaction().begin();

        // Requête HQL
        String hql = "SELECT e.id, e.date, u.prenom, u.nom, c.nom " +
                "FROM Emargements e " +
                "JOIN e.professeur u " +
                "JOIN e.cours c";

        // Exécution de la requête
        Query query = db.createQuery(hql);
        List<Object[]> results = query.getResultList();

        // Liste Observable
        ObservableList<EmargementsDTO> emargementsList = FXCollections.observableArrayList();

        // Traitement des résultats
        for (Object[] row : results) {
            Integer id = ((Long) row[0]).intValue(); // ✅ Convertir Long en Integer
            LocalDate localDate = (LocalDate) row[1];
            Date date = java.sql.Date.valueOf(localDate); // ✅ Conversion LocalDate -> Date
            String prenom = (String) row[2];
            String nom = (String) row[3];
            String coursNom = (String) row[4];

            // ✅ Concaténer prénom et nom pour le prof
            String nomCompletUser = prenom + " " + nom;

            // ✅ Ajouter à la liste
            EmargementsDTO emargement = new EmargementsDTO(id, coursNom, date, nomCompletUser);
            emargementsList.add(emargement);
        }

        // Retourner la liste Observable
        return emargementsList;
    }
    public static ObservableList<EmargementsDTO> chargerEmargements(LocalDate startDate, LocalDate endDate) {
        // Connexion à la base de données
        DBConnexion dbConnexion = new DBConnexion();
        EntityManager db = dbConnexion.getDb();
        db.getTransaction().begin();

        // Requête HQL avec condition sur l'intervalle de dates
        String hql = "SELECT e.id, e.date, u.prenom, u.nom, c.nom " +
                "FROM Emargements e " +
                "JOIN e.professeur u " +
                "JOIN e.cours c " +
                "WHERE e.date BETWEEN :startDate AND :endDate";

        // Exécution de la requête
        Query query = db.createQuery(hql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Object[]> results = query.getResultList();

        // Liste Observable
        ObservableList<EmargementsDTO> emargementsList = FXCollections.observableArrayList();

        // Traitement des résultats
        for (Object[] row : results) {
            Integer id = ((Long) row[0]).intValue(); // ✅ Convertir Long en Integer
            LocalDate localDate = (LocalDate) row[1];
            Date date = java.sql.Date.valueOf(localDate); // ✅ Conversion LocalDate -> Date
            String prenom = (String) row[2];
            String nom = (String) row[3];
            String coursNom = (String) row[4];

            // ✅ Concaténer prénom et nom pour le prof
            String nomCompletUser = prenom + " " + nom;

            // ✅ Ajouter à la liste
            EmargementsDTO emargement = new EmargementsDTO(id, coursNom, date, nomCompletUser);
            emargementsList.add(emargement);
        }

        db.getTransaction().commit(); // ✅ Commit transaction
        db.close(); // ✅ Fermer la connexion

        // Retourner la liste Observable
        return emargementsList;
    }


}

