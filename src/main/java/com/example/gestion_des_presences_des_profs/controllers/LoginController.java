package com.example.gestion_des_presences_des_profs.controllers;
import com.example.gestion_des_presences_des_profs.dao.DBConnexion;
import com.example.gestion_des_presences_des_profs.models.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button btLog;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin() {
        String nom = usernameField.getText();
        String password = passwordField.getText();

        // Vérification des identifiants dans la base de données
        Users user = getByNomAndPassword(nom, password);

        if (user != null && password.equals(user.getPassword())) {
            // Si l'utilisateur existe et le mot de passe est correct
            System.out.println("Connexion réussie !");
            // Charger la page d'accueil après la connexion réussie
            loadHomePage(user);

        } else {
            // Si l'utilisateur ou le mot de passe est incorrect
            showAlert("Erreur", "Nom ou mot de passe incorrect.");
        }

    }
    private void showAlert(String title, String message) {
        // Crée une alerte de type erreur
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);        // Titre de l'alerte
        alert.setHeaderText(null);     // Pas de texte d'en-tête
        alert.setContentText(message); // Message d'alerte

        // Affiche l'alerte et attend qu'elle soit fermée
        alert.showAndWait();
    }


    @FXML
    public void handleForgotPassword() {
        showAlert(Alert.AlertType.INFORMATION, "Mot de passe oublié", "Veuillez contacter l'administrateur.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /*private void loadHomePage() {
        // Charger la page d'accueil après une connexion réussie
        try {
            // Charge le fichier FXML de la page d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/acceuil.fxml"));
            Parent homePage = loader.load();
            // Obtient la scène actuelle
            Scene scene = new Scene(homePage);

            // Obtenir la fenêtre (Stage) actuelle et l'initialiser avec la scène d'accueil
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private void loadHomePage(Users user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/acceuil.fxml"));
            Parent homePage = loader.load();

            // Récupérer le contrôleur de la page d'accueil
            AcceuilController acceuilController = loader.getController();
            acceuilController.setCurrentUser(user);  // Passer l'utilisateur connecté

            // Obtenir la scène actuelle
            Scene scene = new Scene(homePage);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page d'accueil.");
        }
    }

    //acceder a la base
    public Users getByNomAndPassword(String nom, String password) {
        DBConnexion db = new DBConnexion();
        EntityManager db1 = db.getDb();

        // Requête JPQL (Java Persistence Query Language)
        String jpql = "SELECT u FROM Users u WHERE u.nom = :nom AND u.password = :password";

        try {
            // Création de la requête avec les paramètres
            TypedQuery<Users> query = db1.createQuery(jpql, Users.class);
            query.setParameter("nom", nom);  // Utilisation de 'nom' passé en paramètre
            query.setParameter("password", password);  // Utilisation de 'password' passé en paramètre

            // Récupérer le premier résultat correspondant à la requête
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Si aucun utilisateur n'est trouvé, renvoyer null
            return null;
        }
    }






    //acceder a la base
}