package com.example.gestion_des_presences_des_profs.controllers;


import com.example.gestion_des_presences_des_profs.dao.UsersImpl;
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

public class UsersController {
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button ajouterButton;
    @FXML private Button annulerButton;
    @FXML private TableView<Users> tableView;
    @FXML private TableColumn<Users, Long> idColumn;
    @FXML private TableColumn<Users, String> nomColumn;
    @FXML private TableColumn<Users, String> prenomColumn;
    @FXML private TableColumn<Users, String> emailColumn;
    @FXML private TableColumn<Users, String> roleColumn;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;

    private final UsersImpl usersRepository = new UsersImpl();
    private final ObservableList<Users> utilisateurs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Lier les colonnes aux attributs de l'entité
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));


        // Charger les données de la base
        chargerUtilisateurs();

        // Ajouter un utilisateur
        ajouterButton.setOnAction(event -> ajouterUtilisateur());

        // Supprimer un utilisateur
        supprimerButton.setOnAction(event -> supprimerUtilisateur());

        // Annuler (vider les champs)
        annulerButton.setOnAction(event -> viderFormulaire());
    }

    private void chargerUtilisateurs() {
        List<Users> list = usersRepository.getAll();
        utilisateurs.setAll(list);
        tableView.setItems(utilisateurs);
    }

    private void ajouterUtilisateur() {
        if (!validateFields()){
            return;
        }
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();



        Users utilisateur = new Users(nom, prenom, email, password, role);
        usersRepository.add(utilisateur);
        chargerUtilisateurs();
        viderFormulaire();
    }

    private void supprimerUtilisateur() {
        Users row = tableView.getSelectionModel().getSelectedItem();
        Long id=row.getId();
        Users selected=usersRepository.getById(Math.toIntExact(id));
        // Création d'une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Supprimer l'utilisateur");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");

        // Affichage de l'alerte et attente de la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            usersRepository.delete(selected);
            chargerUtilisateurs();
            showAlert("Succès", "L'utilisateur a été supprimé avec succès !");
        }

        /*if (selected != null) {
            usersRepository.delete(selected);
            chargerUtilisateurs();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur !");
        }*/
    }

    private void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void save(ActionEvent event) {
         ajouterUtilisateur();
    }
    @FXML
    void supprimer(ActionEvent event) {
      supprimerUtilisateur();
    }
    @FXML
    private void getData(MouseEvent event) {
        if (event.getClickCount() == 2) { // Vérifie le double-clic
            Users selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                remplirChamps(selectedUser); // Remplit les champs avec les données récupérées
            }
        }
    }
    @FXML
    void update(ActionEvent event) {
        Users selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur !");
            return;
        }
        if (!validateFields()){
            return;
        }
        // Mettre à jour les propriétés de l'utilisateur avec les nouvelles valeurs des champs
        selectedUser.setNom(nomField.getText());
        selectedUser.setPrenom(prenomField.getText());
        selectedUser.setEmail(emailField.getText());
        selectedUser.setPassword(passwordField.getText());
        selectedUser.setRole(roleComboBox.getValue());

        // Mettre à jour l'utilisateur dans la base de données via le repository
        usersRepository.update(selectedUser);

        chargerUtilisateurs();
    }
    private void remplirChamps(Users user) {
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            passwordField.setText(user.getPassword()); // Ne jamais pré-remplir un mot de passe pour la sécurité
            roleComboBox.setValue(user.getRole()); // Sélectionne le rôle
        }
    }
    private boolean validateFields() {
        // Vérification si les champs sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() || roleComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return false;
        }

        // Vérification du format du nom (doit contenir uniquement des lettres)
        if (!nomField.getText().matches("[a-zA-Z]+")) {
            showAlert("Erreur", "Le nom ne peut contenir que des lettres !");
            return false;
        }

        // Vérification du format du prénom (doit contenir uniquement des lettres)
        if (!prenomField.getText().matches("[a-zA-Z]+")) {
            showAlert("Erreur", "Le prénom ne peut contenir que des lettres !");
            return false;
        }

        // Vérification du format de l'email (expression régulière simple pour un email valide)
        String email = emailField.getText();
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            showAlert("Erreur", "L'email n'est pas valide !");
            return false;
        }

        // Vérification de la longueur du mot de passe (minimum 8 caractères)
        String password = passwordField.getText();
        if (password.length() < 8) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 8 caractères !");
            return false;
        }

        // Vérification que le rôle est sélectionné
        if (roleComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un rôle !");
            return false;
        }

        return true;
    }



}

