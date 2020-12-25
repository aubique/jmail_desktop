package com.jmail.controller.services;

import com.jmail.model.EmailMessage;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class MessageRendererService extends Service {

    private final WebEngine webEngine;
    private final StringBuffer stringBuffer;
    private EmailMessage emailMessage;

    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.stringBuffer = new StringBuffer();
        this.setOnSucceeded(event -> displayMessage());
    }

    private static boolean isSimpleType(String contentType) {
        return contentType.contains("TEXT/HTML") ||
                contentType.contains("mixed") ||
                contentType.contains("text");
    }

    private static boolean isMultipartType(String contentType) {
        return contentType.contains("multipart");
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    private void displayMessage() {
        webEngine.loadContent(stringBuffer.toString());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void loadMessage() throws MessagingException, IOException {
        this.stringBuffer.setLength(0); //clears the SB
        final Message message = emailMessage.getMessage();
        String contentType = message.getContentType();
        if (isSimpleType(contentType))
            this.stringBuffer.append(message.getContent().toString());
        else if (isMultipartType(contentType)) {
            final var multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart initMultipart, StringBuffer stringBuffer)
            throws MessagingException, IOException {
        for (int i = initMultipart.getCount() - 1; i >= 0; i--) {
            final BodyPart bodyPart = initMultipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if (isSimpleType(contentType))
                stringBuffer.append(bodyPart.getContent().toString());
            else if (isMultipartType(contentType)) {
                final var multipart = (Multipart) bodyPart.getContent();
                loadMultipart(multipart, stringBuffer);
            } else if (!isTextPlain(contentType)) {
                // get the attachments:
                final var mimeBodyPart = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(mimeBodyPart);
            }
        }
    }

    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT/PLAIN");
    }
}
