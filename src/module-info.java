module JavaFxEmailClient {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;
    requires java.desktop;

    opens com.jmail;
    opens com.jmail.view;
    opens com.jmail.controller;
    opens com.jmail.model;
}
