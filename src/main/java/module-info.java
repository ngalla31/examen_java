module com.example.gestion_des_presences_des_profs {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires lombok;
    requires java.desktop;
    requires itextpdf;
    requires java.mail;
    //requires jakarta.transaction;
    requires java.management;


    opens com.example.gestion_des_presences_des_profs to javafx.fxml;
    exports com.example.gestion_des_presences_des_profs;
    opens com.example.gestion_des_presences_des_profs.models to javafx.fxml, org.hibernate.orm.core;
    exports com.example.gestion_des_presences_des_profs.models;
    opens com.example.gestion_des_presences_des_profs.controllers to javafx.fxml;
    exports com.example.gestion_des_presences_des_profs.controllers;
    opens com.example.gestion_des_presences_des_profs.dao to javafx.fxml;
    exports com.example.gestion_des_presences_des_profs.dao;
}