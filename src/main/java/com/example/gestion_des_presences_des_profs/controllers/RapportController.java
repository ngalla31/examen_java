package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.models.EmargementsDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.example.gestion_des_presences_des_profs.models.EmargementsDTO.chargerEmargements;

public class RapportController {
    EmargementsDTO emargementsDTO=new EmargementsDTO();
    @FXML
    private Button exportPDF;
    @FXML
    private Button btRechercher;

    @FXML
    private TableColumn<EmargementsDTO, String> cCours;

    @FXML
    private TableColumn<EmargementsDTO, Date> cDate;

    @FXML
    private TableColumn<EmargementsDTO, Integer> cId;

    @FXML
    private TableColumn<EmargementsDTO, String> cProf;
    @FXML
    private DatePicker end;

    @FXML
    private DatePicker start;

    @FXML
    private TableView<EmargementsDTO> table;

    @FXML
    private void exportToPDF() {
        //String file = "C:\\Users\\hp\\Desktop\\exos_java";
        String file = "C:\\Users\\hp\\Desktop\\exos_java\\tableau_exporte.pdf";
        exportTableToPDF(table,file);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void rechercher(ActionEvent actionEvent) {
        LocalDate debut=start.getValue();
        LocalDate fin=end.getValue();
        checkDates(debut,fin);
        //EmargementsDTO emargementsDTO1=new EmargementsDTO();
        ObservableList<EmargementsDTO>list=chargerEmargements(debut,fin);
        //ObservableList<EmargementsDTO> emargements = emargementsDTO.chargerEmargements();
        cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cCours.setCellValueFactory(new PropertyValueFactory<>("cours"));
        cProf.setCellValueFactory(new PropertyValueFactory<>("nomCompletUser"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        //chargerEmargements();
        // Ajouter la liste au TableView
        table.setItems(list);
    }

    public void checkDates(LocalDate start, LocalDate end) {
        // Récupérer les valeurs des dates sélectionnées
        //LocalDate debut = start.getValue();
        //LocalDate fin = end.getValue();

        // Vérifier si les dates sont nulles
        if (start == null || end == null) {
            showAlert(Alert.AlertType.WARNING, "Erreur de sélection", "Une des dates est invalide. Veuillez sélectionner les deux dates.");
            return; // Sortir si une des dates est null
        }

        // Comparer les dates
        if (start.isBefore(end)) {
            // Les dates sont dans l'ordre correct
            // (Tu peux afficher un message si nécessaire, ici il n'y en a pas)
        } else if (start.isEqual(end)) {
            // Si les dates sont égales, afficher une alerte
            showAlert(Alert.AlertType.INFORMATION, "Alerte", "La date de début est égale à la date de fin.");
        } else {
            // Si la date de début est après la date de fin, afficher une alerte d'erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de début ne peut pas être après la date de fin.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Fonction pour exporter un tableau sous format PDF
    public void exportTableToPDF(TableView<EmargementsDTO> tableView, String fileName) {
        // Création du document PDF
        Document document = new Document();

        try {
            // Définir le fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream(fileName));

            // Ouvrir le document
            document.open();

            // Ajouter un titre au PDF
            document.add(new Paragraph("Export du Tableau", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD)));

            // Créer le tableau PDF
            PdfPTable pdfTable = new PdfPTable(tableView.getColumns().size()); // Le nombre de colonnes

            // Ajouter les en-têtes du tableau
            for (TableColumn<?, ?> column : tableView.getColumns()) {
                pdfTable.addCell(column.getText());
            }

            // Ajouter les lignes du tableau
            List<EmargementsDTO> items = tableView.getItems();
            for (EmargementsDTO item : items) {
                for (TableColumn<EmargementsDTO, ?> column : tableView.getColumns()) {
                    // Récupérer la valeur de chaque cellule pour chaque ligne
                    Object cellData = column.getCellData(item);
                    pdfTable.addCell(cellData != null ? cellData.toString() : ""); // Ajouter la donnée de la cellule, ou une chaîne vide si c'est null
                }
            }

            // Ajouter le tableau au document
            document.add(pdfTable);

            // Fermer le document
            document.close();

            //System.out.println("PDF généré avec succès !");
            showAlert(Alert.AlertType.INFORMATION, "Alerte", "PDF généré avec succès !");
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }

      }
}








