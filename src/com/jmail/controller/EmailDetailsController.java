package com.jmail.controller;

import com.jmail.EmailManager;
import com.jmail.controller.services.MessageRendererService;
import com.jmail.model.EmailMessage;
import com.jmail.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    @FXML
    private WebView webView;

    @FXML
    private Label attachmentLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private HBox hBoxDownloads;

    private String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/dl";

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        loadAttachments(emailMessage);

        final var messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    private void loadAttachments(EmailMessage emailMessage) {
        if (emailMessage.hasAttachments())
            for (MimeBodyPart mimeBodyPart : emailMessage.getAttachmentList()) {
                try {
                    final var button = new Button(mimeBodyPart.getFileName());
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            }
        else
            attachmentLabel.setText("");
    }
}
