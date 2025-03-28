package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.dao.SalleImpl;
import com.example.gestion_des_presences_des_profs.models.Salle;
import com.example.gestion_des_presences_des_profs.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Optional;

public class SalleController {
   SalleImpl salle=new SalleImpl();

    @FXML
    private Button ajouterButton;

    @FXML
    private Button annulerButton;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> libelleColumn;

    @FXML
    private TextField libelleField;

    @FXML
    private Button modifierButton;

    @FXML
    private Button supprimerButton;

    @FXML
    private TableView<Salle> tableSalle;

    @FXML
    public void initialize() {
        // Lier les colonnes aux attributs de l'entité Salle
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));

        // Charger les données de la base
        chargerSalles();

        // Ajouter une salle

    }

    @FXML
    void ajouterSalle(ActionEvent event) {
        if(!validateFields()){return;}
        String nom = libelleField.getText();
        Salle salle1=new Salle(libelleField.getText());
        salle.add(salle1);
        chargerSalles();
    }

    @FXML
    void annuler(ActionEvent event) {
         libelleField.setText("");
    }
    @FXML
    void getData(MouseEvent event) {
        if (event.getClickCount() == 2) { // Vérifie le double-clic
            Salle selectedSalle = tableSalle.getSelectionModel().getSelectedItem();
            if (selectedSalle != null) {
                remplirChamps(selectedSalle); // Remplit les champs avec les données récupérées
            }
        }
    }


    @FXML
    void modifierSalle(ActionEvent event) {
        Salle selectedSalle = (Salle) tableSalle.getSelectionModel().getSelectedItem();
        if (selectedSalle == null) {
            showAlert("Erreur", "Veuillez sélectionner une salle !");
            return;
        }

        if (!validateFields()) {
            return;
        }

// Mettre à jour les propriétés de la salle avec les nouvelles valeurs du champ
        selectedSalle.setLibelle(libelleField.getText());

// Mettre à jour la salle dans la base de données via le repository
        salle.update(selectedSalle);

// Recharger les données de la table après modification
        chargerSalles();

    }

    @FXML
    void supprimerSalle(ActionEvent event) {
        Salle row = tableSalle.getSelectionModel().getSelectedItem();
        Long id = row.getId(); // Récupère l'ID de la salle sélectionnée
        Salle selected = salle.getById(Math.toIntExact(id)); // Récupère la salle par son ID
        // Création d'une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Supprimer la salle");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce cours ?");

        // Affichage de l'alerte et attente de la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            salle.delete(selected);
            chargerSalles();
            showAlert("Succès", "La salle a été supprimé avec succès !");
        }  else {
        showAlert("Erreur", "Veuillez sélectionner une salle !");
        }
        /*if (selected != null) {
            salle.delete(selected); // Supprime la salle
            chargerSalles(); // Recharge la liste des salles
        } else {
            showAlert("Erreur", "Veuillez sélectionner une salle !");
        }*/

    }
    private boolean validateFields() {
        // Vérification si le champ est vide
        if (libelleField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir le champ Libellé !");
            return false;
        }

        // Vérification du format du libellé (doit contenir uniquement des lettres et espaces)
        if (!libelleField.getText().matches("[a-zA-Z0-9 ]+")) {
            showAlert("Erreur", "Le libellé ne peut contenir que des lettres, des chiffres et des espaces !");
            return false;
        }


        return true; // Retourne vrai si tout est valide
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void chargerSalles() {
        // Récupère toutes les salles depuis le repository
        List<Salle> list = salle.getAll();


        // Met à jour la liste des salles dans l'ObservableList
        ObservableList<Salle> salles=FXCollections.observableArrayList();;
        salles.setAll(list);

        // Affiche les salles dans le TableView
        tableSalle.setItems(salles);
    }
    public void viderFormulaire(){
        libelleField.setText("");
    }
    private void remplirChamps(Salle salle) {
        if (salle != null) {
            libelleField.setText(salle.getLibelle()); // Remplir le champ libellé avec la valeur de la salle
        }
    }


}
