package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.models.Emargements;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class EmargementImpl implements IRepository<Emargements>{
    DBConnexion dbConnexion = new DBConnexion();
    EntityManager db = dbConnexion.getDb();
    @Override
    public void add(Emargements emargements) {
        db.getTransaction().begin();
        db.persist(emargements);
        db.getTransaction().commit();
    }

    @Override
    public void delete(Emargements emargements) {

    }

    @Override
    public void update(Emargements emargements) {

    }

    @Override
    public Emargements getById(Emargements emargements) {
        return null;
    }

    @Override
    public List<Emargements> getAll() {
        return List.of();
    }

    @Override
    public Emargements getById(int id) {
        return null;
    }
    public static List<Object[]> getEmargementsParProfesseur() {
        DBConnexion dbConnexion1=new DBConnexion();
        EntityManager db= dbConnexion1.getDb();
        List<Object[]> results = null;

        try {

            /*TypedQuery<Object[]> query = db.createQuery(
                    "SELECT u.nom, u.prenom, COUNT(e) " +
                            "FROM Emargements e " +
                            "JOIN e.professeur u " +
                            "WHERE u.role = 'Professeur' " +
                            "GROUP BY u.id", Object[].class);*/
            TypedQuery<Object[]> query = db.createQuery(
                    "SELECT u.nom, COUNT(e) " +
                            "FROM Emargements e " +
                            "JOIN e.professeur u " +
                            "GROUP BY u.id", Object[].class);
            results=query.getResultList();


        } finally {
            db.close();
        }

        return results;
    }
    public static List<Object[]> getEmargementsParPeriode(String periode) {
        DBConnexion dbConnexion1 = new DBConnexion();
        EntityManager db = dbConnexion1.getDb();
        List<Object[]> results = null;


            String queryStr = null;
        if ("jour".equals(periode)) {
            queryStr = "SELECT e.date, COUNT(e) FROM Emargements e GROUP BY e.date ORDER BY e.date";
        } else if ("semaine".equals(periode)) {
            // PostgreSQL utilise EXTRACT(WEEK FROM date)
            queryStr = "SELECT EXTRACT(WEEK FROM e.date) AS semaine, COUNT(e) " +
                    "FROM Emargements e GROUP BY semaine ORDER BY semaine";
        } else if ("mois".equals(periode)) {
            // PostgreSQL utilise EXTRACT(MONTH FROM date)
            queryStr = "SELECT EXTRACT(MONTH FROM e.date) AS mois, COUNT(e) " +
                    "FROM Emargements e GROUP BY mois ORDER BY mois";
        }

// Vérifier que la requête a bien été définie
        if (queryStr != null) {
            Query query = db.createNativeQuery(queryStr);  // Exécuter une requête native SQL
            results = query.getResultList();
        }
        return results;
    }
    // Récupérer le taux de présence par cours
    public static List<Object[]> getPresenceRateByCours() {
        DBConnexion dbConnexion1 = new DBConnexion();
        EntityManager db = dbConnexion1.getDb();
        String queryStr = "SELECT c.nom, " +
                "  COUNT(CASE WHEN e.id IS NOT NULL THEN 1 ELSE NULL END) AS emargements_present, " +
                "  COUNT(CASE WHEN e.id IS NULL THEN 1 ELSE NULL END) AS emargements_absent " +
                "FROM Cours c " +
                "LEFT JOIN c.emargements e " +
                "GROUP BY c.id";





        TypedQuery<Object[]> query = db.createQuery(queryStr, Object[].class);
        return query.getResultList();  // Retourne une liste d'objets avec le nombre de présences et d'absences par cours
    }

}
