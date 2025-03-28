package com.example.gestion_des_presences_des_profs.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;

import java.util.List;

import static com.example.gestion_des_presences_des_profs.dao.EmargementImpl.*;

public class StatistiquesController {
    @FXML
    private BarChart<String, Number> barChartProf;

    @FXML
    private LineChart<String, Number> lineChartEmargements;

    @FXML
    private PieChart pieChartPresence;
    @FXML
    private TextField periodeTextField;

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private CategoryAxis xAxis1;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private NumberAxis yAxis1;



    @FXML
    public void initialize() {
        loadBarChartData();
        loadLineChartData();
        loadPieChartData();
    }

    private void loadBarChartData() {
        List<Object[]> data = getEmargementsParProfesseur();
        if (data == null || data.isEmpty()) {
            System.out.println("Aucune donnée disponible pour le graphique.");
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences par Professeur");

        for (Object[] row : data) {
            String nom = (String) row[0];  // Nom du professeur
            Number total = (Number) row[1];  // Nombre total d'émargements

            series.getData().add(new XYChart.Data<>(nom, total));
        }

        barChartProf.getData().clear();  // Nettoyer d'anciennes données s'il y en a
        barChartProf.getData().add(series);
    }
    /*private void loadBarChartData() {
        List<Object[]> data = getEmargementsParProfesseur();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Présences par Professeur");

        for (Object[] row : data) {
            String nom = (String) row[0];  // Nom du professeur
            Number total = (Number) row[1];  // Nombre total d'émargements

            series.getData().add(new XYChart.Data<>(nom, total));
        }

        barChartProf.getData().clear();  // Nettoyer d'anciennes données s'il y en a
        barChartProf.getData().add(series);
    }*/

    @FXML
    private void loadLineChartData() {
        String periode=periodeTextField.getText();
        List<Object[]> data = getEmargementsParPeriode(periode);

        if (data == null || data.isEmpty()) {
            System.out.println("Aucune donnée disponible pour le graphique.");
            return;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Évolution des émargements");

        for (Object[] row : data) {
            String period = null;
            if (periode.equals("jour")) {
                period = row[0].toString();  // Date
            } else if (periode.equals("semaine")) {
                period = "Semaine " + row[0].toString();  // Semaine
            } else if (periode.equals("mois")) {
                period = "Mois " + row[0].toString();  // Mois
            }

            Number count = (Number) row[1];  // Nombre d'émargements
            series.getData().add(new XYChart.Data<>(period, count));
        }

        lineChartEmargements.getData().clear();  // Nettoyer d'anciennes données
        lineChartEmargements.getData().add(series);
    }

    private void loadPieChartData() {
        // Récupérer les données de présence et d'absence depuis la base de données
        List<Object[]> result = getPresenceRateByCours();

        // Initialisation des données pour le graphique
        for (Object[] row : result) {
            String cours = (String) row[0];  // Nom du cours
            long presences = (Long) row[1];  // Nombre de présences
            long absences = (Long) row[2];  // Nombre d'absences

            // Calcul du pourcentage de présence
            double total = presences + absences;
            double presenceRate = (presences / total) * 100;
            double absenceRate = (absences / total) * 100;

            // Créer les données du PieChart pour chaque cours
            PieChart.Data presenceData = new PieChart.Data(cours + " - Présences", presenceRate);
            PieChart.Data absenceData = new PieChart.Data(cours + " - Absences", absenceRate);

            // Ajouter les données au PieChart
            pieChartPresence.getData().clear();  // Vider les anciennes données
            pieChartPresence.getData().addAll(presenceData, absenceData);
        }
    }


}
