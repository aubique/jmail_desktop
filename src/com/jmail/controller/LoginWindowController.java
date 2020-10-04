package com.jmail.controller;

import com.jmail.EmailManager;
import com.jmail.controller.services.LoginService;
import com.jmail.model.EmailAccount;
import com.jmail.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailAddressField;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        System.out.println("loginButtonAction");

        if (areFieldsValid()) {
            final var emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            final var loginService = new LoginService(emailAccount, emailManager);

            loginService.start();
            loginService.setOnSucceeded(event -> {
                final EmailLoginResult emailLoginResult = loginService.getValue();
                switch (emailLoginResult) {
                    case SUCCESS:
                        System.out.println("login successful!" + emailAccount);
                        viewFactory.showMainWindow();
                        final Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                }
            });
        }

        System.out.println("loginButtonAction");
    }

    private boolean areFieldsValid() {
        if (emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please fill the email field");
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill password");
            return false;
        }

        return true;
    }
}
