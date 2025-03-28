package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.models.Cours;
import com.example.gestion_des_presences_des_profs.models.Salle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class SalleImpl implements IRepository<Salle> {
    DBConnexion dbConnexion=new DBConnexion();
    EntityManager db = dbConnexion.getDb();
    @Override
    public void add(Salle salle) {
       db.getTransaction().begin();
       db.persist(salle);
       db.getTransaction().commit();
    }

    @Override
    public void delete(Salle salle) {
        db.remove(salle);
        db.getTransaction().commit();
    }

    @Override
    public void update(Salle salle) {
       db.getTransaction().begin();
       db.merge(salle);
       db.getTransaction().commit();
    }

    @Override
    public Salle getById(Salle salle) {
        return null;
    }

    @Override
    public List<Salle> getAll() {
        EntityManager db = dbConnexion.getDb();
        List<Salle> salles = null;

        try {
            db.getTransaction().begin();
            // Requête pour récupérer toutes les salles
            Query query = db.createQuery("SELECT s FROM Salle s");
            salles = query.getResultList();
            db.getTransaction().commit();
        } catch (RuntimeException e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e;
        }

        return salles;
    }

    @Override
    public Salle getById(int id) {
        db.getTransaction().begin();
        Salle selected=db.find(Salle.class,id);
        return selected;
    }



}
