package com.example.gestion_des_presences_des_profs.controllers;

import com.example.gestion_des_presences_des_profs.dao.NotificationsImpl;
import com.example.gestion_des_presences_des_profs.models.NotificationData;
import com.example.gestion_des_presences_des_profs.models.Notifications;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class NotificationsController {
        NotificationsImpl notification=new NotificationsImpl();
        @FXML
        private TableColumn<Notifications, Date> dateEnvoiColumn;

        @FXML
        private TableColumn<Notifications, String> destinataireColumn;

        @FXML
        private TableColumn<Notifications, String> messageColumn;
        @FXML
        private TableView<NotificationData> notificationTable;
    public void initialize() {
        // Initialiser les colonnes de la TableView
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        destinataireColumn.setCellValueFactory(new PropertyValueFactory<>("destinataire"));
        dateEnvoiColumn.setCellValueFactory(new PropertyValueFactory<>("dateEnvoi"));

        // Récupérer les notifications depuis le service
       // ObservableList<NotificationData> notifications = notification.getNotifications();
        // Récupérer les notifications depuis le service
        ObservableList<NotificationData> notifications = FXCollections.observableArrayList(notification.getNotifications());

        // Mettre à jour la TableView avec les notifications
        notificationTable.setItems(notifications);

    }


}
