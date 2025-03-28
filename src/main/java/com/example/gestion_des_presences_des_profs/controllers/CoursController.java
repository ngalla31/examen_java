package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.dao.*;
import com.example.gestion_des_presences_des_profs.models.*;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class CoursController {
    CoursImpl coursImpl = new CoursImpl();
    EmailService emailService=new EmailService();
    @FXML
    private TextField nomField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField heureDebutField;
    @FXML
    private TextField heureFinField;
    @FXML
    private ComboBox<Salle> salleComboBox;
    @FXML
    private ComboBox<Users> profComboBox;
    @FXML
    private TableView<Cours> tableView;
    @FXML
    private TableColumn<Cours, Integer> idColumn;
    @FXML
    private TableColumn<Cours, String> nomColumn;
    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    private TableColumn<Cours, String> heureDebutColumn;
    @FXML
    private TableColumn<Cours, String> heureFinColumn;
    @FXML
    private TableColumn<Cours, Salle> salleColumn;
    @FXML
    private TableColumn<Cours, Salle> professeurColumn;
    private CoursImpl coursRepository = new CoursImpl();
    private SalleImpl salleRepository = new SalleImpl();
    private UsersImpl usersRepository=new UsersImpl();
    NotificationsImpl notif=new NotificationsImpl();
    DBConnexion dbConnexion = new DBConnexion();
    EntityManager db = dbConnexion.getDb();

    @FXML
    public void initialize() {

        // Lier les colonnes aux attributs de l'entité Cours
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        heureDebutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        heureFinColumn.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        //salleColumn.setCellValueFactory(new PropertyValueFactory<>("salle"));
        professeurColumn.setCellValueFactory(new PropertyValueFactory<>("nomCompletUser"));
        salleColumn.setCellValueFactory(new PropertyValueFactory<>("libelleSalle"));


        // Charger les données des salles dans le ComboBox
        List<Salle> salles = salleRepository.getAll();
        salleComboBox.setItems(FXCollections.observableArrayList(salles));
      // Afficher uniquement les libellés dans la liste déroulante
        salleComboBox.setCellFactory(lv -> new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle salle, boolean empty) {
                super.updateItem(salle, empty);
                setText((salle == null || empty) ? null : salle.getLibelle());
            }
        });

// Assurer que le libellé est affiché après sélection
        salleComboBox.setButtonCell(new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle salle, boolean empty) {
                super.updateItem(salle, empty);
                setText((salle == null || empty) ? null : salle.getLibelle());
            }
        });
        // Charger les profs dans le ComboBox
        List<Users> profs = usersRepository.getProfesseurs();

// Vérifier si la liste des profs est bien remplie
        if (profs != null && !profs.isEmpty()) {
            profComboBox.setItems(FXCollections.observableArrayList(profs));

            // Personnaliser l'affichage des éléments dans la liste déroulante du ComboBox
            profComboBox.setCellFactory(param -> new ListCell<Users>() {
                @Override
                protected void updateItem(Users item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Afficher le nom et prénom du professeur
                        setText(item.getPrenom() + " " + item.getNom());
                    }
                }
            });

            // Pour afficher correctement l'élément sélectionné dans le bouton du ComboBox
            profComboBox.setButtonCell(new ListCell<Users>() {
                @Override
                protected void updateItem(Users item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        // Afficher le nom et prénom dans le bouton du ComboBox
                        setText(item.getPrenom() + " " + item.getNom());
                    }
                }
            });
        } else {
            // Si la liste est vide ou null, afficher un message d'erreur ou une valeur par défaut
            profComboBox.setPromptText("Aucun professeur disponible");
        }


        // Charger les cours dans la table
        chargerCours();
    }

    @FXML
    private void ajouterCours() {
        if(!validateFields()){return ;}
        String nom = nomField.getText();
        String description = descriptionField.getText();
        LocalTime heureDebut = LocalTime.parse(heureDebutField.getText());
        LocalTime heureFin = LocalTime.parse(heureFinField.getText());
        Salle salle = salleComboBox.getValue();
        Users users=profComboBox.getValue();

        //validateFields(nom,description,heureDebut,heureFin,salle);
        Users selectedUser = profComboBox.getSelectionModel().getSelectedItem();
        Long userId=selectedUser.getId();

        if(coursImpl.getCoursByUserIdAndHeureDebut(userId,heureDebut)==null)
        {
            Cours cours = new Cours(nom, description, heureDebut, heureFin,users, salle);
            coursImpl.add(cours);

            // 📧 Envoyer un email et enregistrer la notification
            String emailProf = selectedUser.getEmail();
            String sujet = "📅 Nouveau cours ajouté";
            String message = "Bonjour " + selectedUser.getNom() + ",\n\n"
                    + "Un nouveau cours a été ajouté : " + cours.getNom() + "\n"
                    + "⏰ Heure : " + cours.getHeureDebut() + "\n\n"
                    + "Merci et bonne journée !";
            emailService.envoyerEmail(emailProf, sujet, message);
            LocalDateTime dateEnvoi=LocalDateTime.now();
            Notifications notifications=new Notifications(dateEnvoi,message,selectedUser);
             db.getTransaction().begin();
             db.persist(notifications);
             db.getTransaction().commit();

        };


        chargerCours();
        viderFormulaire();
    }

    @FXML
    void getData(ActionEvent event) {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        remplirChamps(selectedCours);
    }

    @FXML
    private void modifierCours() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();

        if (selectedCours == null) {
            showAlert("Erreur", "Veuillez sélectionner un cours !");
            return;
        }

// Vérifier si les champs sont bien remplis
        String nom = nomField.getText();
        String description = descriptionField.getText();
        String heureDebutStr = heureDebutField.getText();
        String heureFinStr = heureFinField.getText();
        //LocalTime heureDebut = selectedCours.getHeureDebut();
        //LocalTime heureFin = selectedCours.getHeureFin();
        Salle salle = salleComboBox.getValue();
        Users users=profComboBox.getValue();

// Vérification des champs obligatoires


            // Convertir les chaînes en objets LocalTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heureDebut = LocalTime.parse(heureDebutStr,formatter); // Convertir en LocalTime
            LocalTime heureFin = LocalTime.parse(heureFinStr,formatter); // Convertir en LocalTime*/

            // Mettre à jour les propriétés du cours
            selectedCours.setNom(nom);
            selectedCours.setDescription(description);
            selectedCours.setHeureDebut(heureDebut);
            selectedCours.setHeureFin(heureFin);
            selectedCours.setSalle(salle);
            selectedCours.setUser(users);
            if (!validateFields()) {
                return;
            }

            // Mettre à jour le cours dans la base de données
            coursRepository.update(selectedCours);

            // Recharger les cours dans la table
            chargerCours();

    }

    @FXML
    private void supprimerCours() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        if (selectedCours == null) {
            showAlert("Erreur", "Veuillez sélectionner un cours !");
            return;
        }
        // Création d'une boîte de dialogue de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Supprimer le cours");
        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce cours ?");

        // Affichage de l'alerte et attente de la réponse de l'utilisateur
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            coursRepository.delete(selectedCours);
            chargerCours();
            showAlert("Succès", "Le cours a été supprimé avec succès !");
        }

        //coursRepository.delete(selectedCours);
        //chargerCours();
    }

    @FXML
    private void annulerFormulaire() {
        viderFormulaire();
    }

    private void viderFormulaire() {
        nomField.clear();
        descriptionField.clear();
        heureDebutField.clear();
        heureFinField.clear();
        salleComboBox.getSelectionModel().clearSelection();
    }

    private void chargerCours() {
        List<Cours> coursList = coursRepository.getAll();
        tableView.setItems(FXCollections.observableArrayList(coursList));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public boolean validateFields(String nom, String description, String heureDebut, String heureFin, Salle salle) {
        // 1. Vérification du nom : doit être non vide et ne contenir que des lettres
        if (nom.isEmpty()) {
            showAlert("Erreur", "Le nom ne peut pas être vide !");
            return false;
        }
        if (!nom.matches("[a-zA-Z ]+")) {  // Autorise uniquement les lettres et les espaces
            showAlert("Erreur", "Le nom ne peut contenir que des lettres et des espaces !");
            return false;
        }

        // 2. Vérification de la description : doit être non vide
        if (description.isEmpty()) {
            showAlert("Erreur", "La description ne peut pas être vide !");
            return false;
        }

        // 3. Vérification des heures : doivent être non vides et respecter le format HH:mm
        if (heureDebut.isEmpty() || heureFin.isEmpty()) {
            showAlert("Erreur", "Les heures de début et de fin ne peuvent pas être vides !");
            return false;
        }

        // Vérifier si les heures sont au bon format
        try {
            LocalTime.parse(heureDebut); // Vérifie que l'heure est au bon format (HH:mm)
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "L'heure de début doit être au format HH:mm !");
            return false;
        }

        try {
            LocalTime.parse(heureFin); // Vérifie que l'heure est au bon format (HH:mm)
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "L'heure de fin doit être au format HH:mm !");
            return false;
        }

        // 4. Vérification de la salle : doit être non nulle
        if (salleComboBox.getSelectionModel().getSelectedItem()  == null) {
            showAlert("Erreur", "La salle doit être sélectionnée !");
            return false;
        }

        // Si tout est valide
        return true;
    }

    private boolean validateFields() {
        // Vérifier si le champ 'nom' n'est pas vide ou null
        if (nomField.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Nom est obligatoire.");
            return false;
        }
        // Vérification du format du prénom (doit contenir uniquement des lettres)
        if (!nomField.getText().matches("[a-zA-Z]+")) {
            showAlert("Erreur", "Le nom ne peut contenir que des lettres !");
            return false;
        }

        // Vérifier si le champ 'description' n'est pas vide ou null
        if (descriptionField.getText().isEmpty()) {
            showAlert("Erreur", "Le champ Description est obligatoire.");
            return false;
        }
        // Vérification du format du prénom (doit contenir uniquement des lettres)
        if (!descriptionField.getText().matches("[a-zA-Z]+")) {
            showAlert("Erreur", "La description ne peut contenir que des lettres !");
            return false;
        }


        // Vérifier si le champ 'heureDebut' n'est pas vide
        String heureDebut = heureDebutField.getText();
        if (heureDebut.isEmpty()) {
            showAlert("Erreur", "Le champ Heure de début est obligatoire.");
            return false;
        }

        // Vérifier si le champ 'heureFin' n'est pas vide
        String heureFin = heureFinField.getText();
        if (heureFin.isEmpty()) {
            showAlert("Erreur", "Le champ Heure de fin est obligatoire.");
            return false;
        }

        // Vérification des formats d'heures (au format HH:mm)
        LocalTime heureDebutParsed;
        LocalTime heureFinParsed;
        try {
            heureDebutParsed = LocalTime.parse(heureDebut); // Vérifie si l'heure de début est valide
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "L'heure de début est invalide. Utilisez le format HH:mm.");
            return false;
        }

        try {
            heureFinParsed = LocalTime.parse(heureFin); // Vérifie si l'heure de fin est valide
        } catch (DateTimeParseException e) {
            showAlert("Erreur", "L'heure de fin est invalide. Utilisez le format HH:mm.");
            return false;
        }

        // Vérifier que l'heure de fin est après l'heure de début
        if (heureFinParsed.isBefore(heureDebutParsed)) {
            showAlert("Erreur", "L'heure de fin ne peut pas être avant l'heure de début.");
            return false;
        }

        // Vérifier si le champ 'salle_id' est sélectionné (ce champ peut être une ComboBox)
        if (salleComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une salle.");
            return false;
        }

        // Vérifier si le champ 'users_id' est sélectionné (ce champ peut être une ComboBox)
        if (profComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un professeur.");
            return false;
        }

        // Si tous les champs sont valides
        return true;
    }


    // Méthode pour remplir les champs avec les valeurs de l'objet Cours sélectionné
    private void remplirChamps(Cours cours) {
        Cours cours1 = tableView.getSelectionModel().getSelectedItem();
        if (cours1 != null) {
            // Remplir les champs avec les données de l'objet Cours
            nomField.setText(cours1.getNom());
            descriptionField.setText(cours1.getDescription());
            heureDebutField.setText(cours1.getHeureDebut().toString()); // Assurez-vous que heureDebut est de type LocalTime
            heureFinField.setText(cours1.getHeureFin().toString()); // Assurez-vous que heureFin est de type LocalTime
            //salleComboBox.setValue(cours1.getSalle()); // Remplir la ComboBox avec la salle
            // Ajouter un StringConverter pour afficher le libellé de la salle dans le ComboBox

            salleComboBox.setCellFactory(param -> new ListCell<Salle>() {
                @Override
                protected void updateItem(Salle item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getLibelle()); // Affiche uniquement le libellé de la salle
                    }
                }
            });

            profComboBox.setCellFactory(param -> new ListCell<Users>() {
                @Override
                protected void updateItem(Users item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getNom() + " " + item.getPrenom()); // Affiche uniquement le nom complet
                    }
                }
            });

// Pour afficher correctement l'utilisateur sélectionné dans le bouton du ComboBox
            profComboBox.setButtonCell(new ListCell<Users>() {
                @Override
                protected void updateItem(Users item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getPrenom() + " " + item.getNom()); // Affiche uniquement le nom complet
                    }
                }
            });


// Maintenant, pour définir la valeur dans le ComboBox, sélectionnez l'objet 'Salle' complet
            Salle selectedSalle = cours1.getSalle(); // récupère l'objet Salle lié au cours
            salleComboBox.setValue(selectedSalle); // Sélectionne l'objet complet Salle dans le ComboBox

        }
    }

    @FXML
    void getData(MouseEvent event) {
        if (event.getClickCount() == 2) { // Vérifier s'il s'agit d'un double-clic
            Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
            if (selectedCours != null) {
                remplirChamps(selectedCours); // Fonction pour remplir les champs du formulaire
            }
            //Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
            //remplirChamps(selectedCours);
        }


    }
}

