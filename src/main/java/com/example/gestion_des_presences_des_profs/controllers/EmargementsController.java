package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.dao.CoursImpl;
import com.example.gestion_des_presences_des_profs.dao.DBConnexion;
import com.example.gestion_des_presences_des_profs.dao.EmargementImpl;
import com.example.gestion_des_presences_des_profs.dao.UsersImpl;
import com.example.gestion_des_presences_des_profs.models.Cours;
import com.example.gestion_des_presences_des_profs.models.Emargements;
import com.example.gestion_des_presences_des_profs.models.EmargementsDTO;
import com.example.gestion_des_presences_des_profs.models.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class EmargementsController {
  CoursImpl cousImpl=new CoursImpl();
  UsersImpl usersImpl=new UsersImpl();
  EmargementsDTO emargementsDTO=new EmargementsDTO();
  EmargementImpl emargementsImpl=new EmargementImpl();
    @FXML
    private Button btAnnuler;

    @FXML
    private Button btValider;

    //@FXML
    //private TableColumn<Cours, String> cCours;
    @FXML
    private TableColumn<EmargementsDTO, String> cCours;
    //@FXML
    //private TableColumn<Date, Date> cDate;
    @FXML
    private TableColumn<EmargementsDTO, Date> cDate;

    //@FXML
    //private TableColumn<Integer, Integer> cId;
    @FXML
    private TableColumn<EmargementsDTO, Integer> cId;

    //@FXML
    //private TableColumn<Users, String> cProf;
    @FXML
    private TableColumn<EmargementsDTO, String> cProf;

    @FXML
    private ComboBox<Cours> comboCours;

    @FXML
    private ComboBox<Users> comboProf;

    @FXML
    private DatePicker date;

    @FXML
    private TableView<EmargementsDTO> table;
  // Méthode d'initialisation appelée après le chargement de l'FXML
  @FXML
  public void initialize() {
    // Exemple de chargement des cours dans le ComboBox
    List<Cours> cousList=cousImpl.getAll();
    comboCours.getItems().setAll(cousList);
    // Afficher uniquement le nom du cours dans la ComboBox
    comboCours.setCellFactory(lv -> new javafx.scene.control.ListCell<Cours>() {
      @Override
      protected void updateItem(Cours item, boolean empty) {
        super.updateItem(item, empty);
        setText((item == null || empty) ? null : item.getNom()); // Supposons que `getNom()` retourne le nom du cours
      }
    });
    // Pour afficher correctement l'élément sélectionné dans le bouton du ComboBox
    comboCours.setButtonCell(new ListCell<Cours>() {
      @Override
      protected void updateItem(Cours item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          // Afficher le nom et prénom dans le bouton du ComboBox
          setText(item.getNom());
        }
      }
    });

    List<Users> usersList=usersImpl.getProfesseurs();
    comboProf.getItems().setAll(usersList); // suppose que usersImpl.getAllUsers() renvoie une liste de Users
    // Afficher le nom complet du professeur dans la ComboBox (zone de liste)
    comboProf.setCellFactory(lv -> new javafx.scene.control.ListCell<Users>() {
      @Override
      protected void updateItem(Users item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
          setText(item.getPrenom()+" "+item.getNom()); // Affiche le nom complet du professeur
        } else {
          setText(null); // Si vide ou non valide, ne rien afficher
        }
      }
    });
    // Pour afficher correctement l'élément sélectionné dans le bouton du ComboBox
    comboProf.setButtonCell(new ListCell<Users>() {
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

    // Vous pouvez également configurer d'autres éléments, comme la TableView
    // Exemple de configuration des colonnes de la TableView (si vous en avez besoin)
   /*cId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
    cCours.setCellValueFactory(cellData -> cellData.getValue().getCoursProperty());
    cDate.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
    cProf.setCellValueFactory(cellData -> cellData.getValue().getProfProperty());*/
    ObservableList<EmargementsDTO> emargements = emargementsDTO.chargerEmargements();
    cId.setCellValueFactory(new PropertyValueFactory<>("id"));
    cCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
    cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    cProf.setCellValueFactory(new PropertyValueFactory<>("nomCompletUser"));
    //chargerEmargements();
    // Ajouter la liste au TableView
    table.setItems(emargements);
  }
  @FXML
  void clear(ActionEvent event) {
    // Réinitialiser les ComboBox
    comboCours.setValue(null); // Efface la sélection dans comboCours
    comboProf.setValue(null);  // Efface la sélection dans comboProf

    // Réinitialiser le DatePicker
    date.setValue(null); // Efface la date sélectionnée dans le DatePicker
  }

  @FXML
  void validarEmargement(ActionEvent event) {
    if(!isSelectionValid()){return;}
    // Récupérer l'heure actuelle
    LocalDateTime currentTime = LocalDateTime.now();  // Heure actuelle avec date et heure
    System.out.println(currentTime);
    // Récupérer les valeurs des ComboBox et DatePicker
    Users selectedProf = comboProf.getValue();  // Professeur sélectionné
    Cours selectedCours = comboCours.getValue();  // Cours sélectionné
    LocalDate jour = date.getValue();  // Date sélectionnée

    // Récupérer l'heure de début et de fin du cours
    LocalTime startTime = selectedCours.getHeureDebut();  // Heure de début du cours
    LocalTime endTime = selectedCours.getHeureFin();  // Heure de fin du cours

    // Créer un LocalDateTime combinant la date sélectionnée et l'heure de début et de fin du cours
    LocalDateTime startDateTime = LocalDateTime.of(jour, startTime);  // Combinaison de la date et de l'heure de début
    LocalDateTime endDateTime = LocalDateTime.of(jour, endTime);  // Combinaison de la date et de l'heure de fin

    // Vérifier si l'enregistrement est autorisé
    if (!isWithinAllowedTimeFrame(currentTime, startDateTime, endDateTime)) {
      showAlert("Avertissement", "Heure non autorisée",
              "Vous ne pouvez émarger que 15 minutes avant le début, 30 minutes après le début ou après la fin du cours.");
      return; // Bloque l'émargement
    }

    // Enregistrement de l'émargement
    Emargements emargements = new Emargements(jour, selectedProf, selectedCours);
    emargementsImpl.add(emargements);

    chargerTab();
    // Succès
    showAlert("Succès", "Émargement enregistré", "L'émargement a été enregistré avec succès.");
    System.out.println("Émargement enregistré.");

  }
  /*private boolean isWithinAllowedTimeFrame(LocalDateTime currentTime, LocalDateTime startTime, LocalDateTime endTime) {
    // Vérifier si l'heure actuelle est dans la fenêtre autorisée : 15 minutes avant le début ou après la fin du cours
    boolean isBeforeStart = currentTime.isBefore(startTime.minusMinutes(15)); // 15 minutes avant le début
    boolean isAfterEnd = currentTime.isAfter(endTime.plusMinutes(15)); // 15 minutes après la fin

    // Debugging : Afficher les états de vérification
    System.out.println("isBeforeStart: " + isBeforeStart);
    System.out.println("isAfterEnd: " + isAfterEnd);

    return isBeforeStart || isAfterEnd;
  }*/
  private boolean isWithinAllowedTimeFrame(LocalDateTime currentTime, LocalDateTime startTime, LocalDateTime endTime) {
    boolean isBeforeStart = currentTime.isBefore(startTime.minusMinutes(15)); // Trop tôt
    boolean isAfterStartPlus30 = currentTime.isAfter(startTime.plusMinutes(30)); // Trop tard après début
    boolean isAfterEnd = currentTime.isAfter(endTime); // Après fin du cours

    return (isBeforeStart || isAfterStartPlus30 || isAfterEnd);
  }
  private void showAlert(String title, String header, String message) {
    // Créer une alerte avec le type WARNING
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);

    // Afficher l'alerte
    alert.showAndWait();
  }
  private boolean isSelectionValid() {
    Users selectedProf = comboProf.getValue();
    Cours selectedCours = comboCours.getValue();
    LocalDate jour = date.getValue();

    if (selectedProf == null) {
      showAlert("Erreur", "Professeur non sélectionné", "Veuillez sélectionner un professeur.");
      return false;
    }

    if (selectedCours == null) {
      showAlert("Erreur", "Cours non sélectionné", "Veuillez sélectionner un cours.");
      return false;
    }

    if (jour == null) {
      showAlert("Erreur", "Date non sélectionnée", "Veuillez sélectionner une date.");
      return false;
    }
    if (jour == null) {
      showAlert("Erreur", "Date non sélectionnée", "Veuillez sélectionner une date.");
      return false;
    }
    // Vérifier si la date sélectionnée est avant aujourd'hui
    if (jour.isBefore(LocalDate.now())) {
      showAlert("Erreur", "Date invalide", "La date sélectionnée ne peut pas être dans le passé.");
      return false;
    }

// Vérifier si la date sélectionnée est après aujourd'hui
    if (jour.isAfter(LocalDate.now())) {
      showAlert("Erreur", "Date invalide", "La date sélectionnée ne peut pas être dans le futur.");
      return false;
    }
    return true; // Tout est bien sélectionné
  }
  public void chargerTab(){
    ObservableList<EmargementsDTO> emargements = emargementsDTO.chargerEmargements();
    cId.setCellValueFactory(new PropertyValueFactory<>("id"));
    cCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
    cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    cProf.setCellValueFactory(new PropertyValueFactory<>("nomCompletUser"));
    //chargerEmargements();
    // Ajouter la liste au TableView
    table.setItems(emargements);

  }


}
