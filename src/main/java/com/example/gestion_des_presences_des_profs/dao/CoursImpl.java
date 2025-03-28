package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.models.Cours;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.scene.control.Alert;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CoursImpl implements IRepository<Cours>{
    DBConnexion dbConnexion = new DBConnexion();
    EntityManager db = dbConnexion.getDb();
    @Override
    public void add(Cours cours) {
        db.getTransaction().begin();
        db.persist(cours);
        db.getTransaction().commit();
    }

    @Override
    public void delete(Cours cours) {
        db.remove(cours);
    }

    @Override
    public void update(Cours cours) {
        db.getTransaction().begin();
        db.merge(cours);
        db.getTransaction().commit();
    }

    @Override
    public Cours getById(Cours cours) {
        return null;
    }

    @Override
    public List<Cours> getAll() {
        List<Cours> cours = new ArrayList<>();

        try {
            db.getTransaction().begin();
            TypedQuery<Cours> query = db.createQuery(
                    "SELECT c FROM Cours c JOIN FETCH c.salle", Cours.class
            );
            cours = query.getResultList();
            db.getTransaction().commit();
        } catch (RuntimeException e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            e.printStackTrace();
        }

        return cours;
    }
    @Override
    public Cours getById(int id) {
        return null;
    }

    public Cours getCoursByUserIdAndHeureDebut(Long userId, LocalTime heureDebut) {
        String jpql = "SELECT c FROM Cours c WHERE c.user.id = :userId AND c.heureDebut = :heureDebut";
        TypedQuery<Cours> query = db.createQuery(jpql, Cours.class);
        query.setParameter("userId", userId);
        query.setParameter("heureDebut", heureDebut);

        List<Cours> results = query.getResultList();

        if (!results.isEmpty()) {
            showAlert("⚠️ Conflit horaire", "Un autre cours est déjà prévu à cette heure !");
            return results.get(0); // Retourne le cours en conflit
        }

        return null; // Aucun conflit
    }

    // Méthode pour afficher une boîte de dialogue JavaFX
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    }



