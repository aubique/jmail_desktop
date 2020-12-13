package com.jmail.controller;

import com.jmail.EmailManager;
import com.jmail.controller.services.EmailSenderService;
import com.jmail.model.EmailAccount;
import com.jmail.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMessageController extends BaseController implements Initializable {

    @FXML
    private TextField recipientTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    public ComposeMessageController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void sendButtonAction() {
        System.out.println(htmlEditor.getHtmlText());
        System.out.println("send button");
        final var emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextField.getText(),
                htmlEditor.getHtmlText()
        );
        emailSenderService.start();
        emailSenderService.setOnSucceeded(e -> {
            EmailSendingResult emailSendingResult = emailSenderService.getValue();
            switch (emailSendingResult) {
                case SUCCESS:
                    var stage = (Stage) recipientTextField.getScene().getWindow();
                    viewFactory.closeStage(stage);
                    break;
                case FAILED_BY_PROVIDER:
                    errorLabel.setText("Provider error!");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    errorLabel.setText("Unexpected error!");
                    break;
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        emailAccountChoice.setItems(emailManager.getEmailAccounts());
        emailAccountChoice.setValue(emailManager.getEmailAccounts().get(0));
    }
}
