package com.jmail;

import com.jmail.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final var viewFactory = new ViewFactory(new EmailManager());
        viewFactory.showLoginWindow();
    }

}
