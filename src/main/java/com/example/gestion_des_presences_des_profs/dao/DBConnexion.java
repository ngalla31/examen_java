package com.example.gestion_des_presences_des_profs.dao;

import com.example.gestion_des_presences_des_profs.JPAUtils;
import jakarta.persistence.EntityManager;

public class DBConnexion {
    private EntityManager db;
    public EntityManager getDb(){
        EntityManager db = JPAUtils.getEntityManagerFactory().createEntityManager();
        return db;
    }
}
