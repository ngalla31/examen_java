package com.example.gestion_des_presences_des_profs.dao;

//import com.example.gestion_des_presences_des_profs.models.Cours;

import com.example.gestion_des_presences_des_profs.models.Cours;

import java.util.List;

public interface IRepository<T> {




    void add(T t);        // Ajouter une entité
    void delete(T t);        // Supprimer une entité par ID
    void update(T t);     // Mettre à jour une entité
    T getById(T t);         // Récupérer une entité par ID

    List<T> getAll();



    T getById(int id);




}
