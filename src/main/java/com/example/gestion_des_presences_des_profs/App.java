package com.example.gestion_des_presences_des_profs;

import com.example.gestion_des_presences_des_profs.dao.DBConnexion;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/pages/login.fxml")); // Vérifie bien que login.fxml est dans 'src/main/resources/pages/'
            Scene scene = new Scene(parent);
            //scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Page de connexion");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur : impossible de charger le fichier FXML !");
        }
    }

    public static void main(String[] args) {
        DBConnexion db=new DBConnexion();
        db.getDb();

        launch(args);
    }
}
