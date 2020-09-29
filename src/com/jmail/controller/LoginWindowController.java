package com.jmail.controller;

import com.jmail.EmailManager;
import com.jmail.controller.services.LoginService;
import com.jmail.model.EmailAccount;
import com.jmail.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindowController extends BaseController {

    @FXML
    private Button errorLabel;

    @FXML
    private TextField emailAddressField;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        if (areFieldsValid()) {
            final var emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            final var loginService = new LoginService(emailAccount, emailManager);
            final var emailLoginResult = loginService.login();

            switch (emailLoginResult) {
                case SUCCESS:
                    System.out.println("login successful!" + emailAccount);
                    return;
            }
        }
        System.out.println("loginButtonAction");
        viewFactory.showMainWindow();
        final Stage stage = (Stage) errorLabel.getScene().getWindow();
        viewFactory.closeStage(stage);
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
