package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.models.Cours;
import com.example.gestion_des_presences_des_profs.models.Users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;

public class UsersImpl implements IRepository<Users> {
    DBConnexion dbConnexion = new DBConnexion();
    EntityManager db = dbConnexion.getDb();

    @Override
    public void add(Users users) {
        db.getTransaction().begin();
        db.persist(users);
        db.getTransaction().commit();
    }

    @Override
    public void delete(Users users) {
        db.remove(users);
        db.getTransaction().commit();
    }

    @Override
    public void update(Users users) {
        db.getTransaction().begin();
        db.merge(users);
        db.getTransaction().commit();
    }

    @Override
    public Users getById(Users users) {
        return null;
    }

    @Override
    public List<Users> getAll() {
        db.getTransaction().begin();
        List<Users> users=db.createQuery("SELECT u FROM Users u", Users.class).getResultList();
        db.getTransaction().commit();
        return users;
    }


    @Override
    public Users getById(int id) {
        db.getTransaction().begin();
        Users selected = db.find(Users.class, id);
        return selected;
    }




    // Méthode pour récupérer les utilisateurs avec le rôle "professeur"
    /*public List<Users> getProfesseurs() {
          db.getTransaction().begin();
        // Requête pour récupérer les utilisateurs dont le rôle est "professeur"
        Query query = db.createQuery("FROM Users WHERE role = :role", Users.class);
        query.setParameter("role", "professeur");  // Filtre par le rôle "professeur"
        return query.getResultList();
    }*/
    public List<Users> getProfesseurs() {
        EntityManager db = dbConnexion.getDb();
        db.getTransaction().begin();
        // Créer la requête HQL pour récupérer les utilisateurs ayant le rôle "professeur"
        Query query = db.createQuery("FROM Users u WHERE u.role = :role", Users.class);
        query.setParameter("role", "Professeur");  // Passer le rôle comme paramètre
        db.getTransaction().commit();
        return query.getResultList();  // Récupérer la liste des utilisateurs
    }


}

