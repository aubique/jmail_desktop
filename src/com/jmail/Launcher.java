package com.jmail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final Parent parent = FXMLLoader.load(getClass().getResource("view/first.fxml"));

        final var scene = new Scene(parent, 300, 250);
        stage.setScene(scene);

        stage.show();
    }

}
