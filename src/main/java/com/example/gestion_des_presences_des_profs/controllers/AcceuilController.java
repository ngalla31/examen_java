package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.models.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AcceuilController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab usersTab;

    @FXML
    private Tab coursesTab;
    @FXML
    private Tab salleTab;


    @FXML
    private Tab attendanceTab;

    @FXML
    private Tab reportsTab;
    @FXML
    private Tab statsTab;

    @FXML
    private Tab notificationsTab;

    private Users currentUser; // Stocke l'utilisateur connecté
    // Méthode pour définir l'utilisateur connecté
    public void setCurrentUser(Users user) {
        this.currentUser = user;
        configureTabsBasedOnRole(); // Configure les onglets en fonction du rôle
    }
    private void configureTabsBasedOnRole() {
        if (currentUser == null) {
            return;
        }

        String role = currentUser.getRole(); // Supposons que `User` a une méthode `getRole()`

        switch (role) {
            case "Admin":
                // Un admin voit tous les onglets
                break;

            case "Professeur":
                tabPane.getTabs().remove(usersTab);  // Supprime l'onglet Gestion des utilisateurs
                tabPane.getTabs().remove(salleTab);
                break;

            case "Gestionnaire":
                //usersTab.setDisable(true); // Désactiver la gestion des utilisateurs
                tabPane.getTabs().remove(usersTab);
                coursesTab.setDisable(false);//Desactiver la gestion des cours
                attendanceTab.setDisable(false); // Désactiver les rapports
                salleTab.setDisable(false);//Desactiver les salles
                notificationsTab.setDisable(false); // Désactiver les notifications
                break;

            default:
                // Rôle inconnu -> cacher tous les onglets sauf émargement
                usersTab.setDisable(true);
                coursesTab.setDisable(true);
                salleTab.setDisable(true);
                reportsTab.setDisable(true);
                notificationsTab.setDisable(true);
                break;
        }
    }

    // Méthode d'initialisation
    @FXML
    private void initialize() {

        // Ajouter des écouteurs d'événements pour chaque onglet
        usersTab.setOnSelectionChanged(event -> {
            if (usersTab.isSelected()) {
                System.out.println("Onglet 'Gestion des Utilisateurs' sélectionné.");
                // Vous pouvez appeler ici la méthode pour charger le contenu de cet onglet

                loadUsersPage();
            }
        });

        coursesTab.setOnSelectionChanged(event -> {
            if (coursesTab.isSelected()) {
                System.out.println("Onglet 'Gestion des Cours' sélectionné.");
                // Charger la page ou le contenu des cours
                loadCoursPage();
            }
        });

        attendanceTab.setOnSelectionChanged(event -> {
            if (attendanceTab.isSelected()) {
                System.out.println("Onglet 'Gestion des Émargements' sélectionné.");
                // Charger la page ou le contenu des émargements
                loadEmargementsPage();
            }
        });
        salleTab.setOnSelectionChanged(event -> {
            if (salleTab.isSelected()) {
                System.out.println("Onglet 'Gestion des Salles' sélectionné.");
                // Charger la page ou le contenu des émargements
                loadSallePage();
            }
        });

        reportsTab.setOnSelectionChanged(event -> {
            if (reportsTab.isSelected()) {
                System.out.println("Onglet 'Rapports et Statistiques' sélectionné.");
                // Charger la page ou le contenu des rapports
                loadRapportsPage();
            }
        });
        statsTab.setOnSelectionChanged(event -> {
            if (statsTab.isSelected()) {
                System.out.println("Onglet 'Statistiques' sélectionné.");
                // Charger la page ou le contenu des rapports
               loadStatsPage();
            }
        });

        notificationsTab.setOnSelectionChanged(event -> {
            if (notificationsTab.isSelected()) {
                System.out.println("Onglet 'Notifications' sélectionné.");
                // Charger la page ou le contenu des notifications
                loadNotifsPage();
            }
        });
    }
    // Méthodes pour charger chaque page FXML
    private void loadUsersPage() {
        loadPage("/pages/users.fxml");
    }
    private void loadSallePage() {
        loadPage("/pages/salle.fxml");
    }
    private void loadCoursPage() {
        loadPage("/pages/cours.fxml");
    }
    private void loadEmargementsPage() {loadPage("/pages/emargements.fxml");}
    private void loadRapportsPage() {loadPage("/pages/rapports.fxml");}
    private void loadStatsPage() {loadPage("/pages/statistiques.fxml");}
    private void loadNotifsPage() {loadPage("/pages/notifications.fxml");}
    // Méthode pour charger une page FXML
    public void loadPage(String fxmlFile) {
        try {
            // Charger le fichier FXML
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlFile)); // Charger le fichier FXML (en s'assurant que le chemin est correct)

            // Créer la scène et la définir
            Scene scene = new Scene(parent);

            // Optionnel : ajouter des styles si nécessaire (par exemple, si vous avez des styles associés)
            // scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            // Créer un stage (fenêtre) et le configurer
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Titre de la fenêtre");  // Vous pouvez ajuster le titre si nécessaire
            stage.setResizable(false);  // Facultatif, ajuster la possibilité de redimensionner la fenêtre
            stage.show();  // Afficher la fenêtre

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur : impossible de charger le fichier FXML !");
        }

    }





}
