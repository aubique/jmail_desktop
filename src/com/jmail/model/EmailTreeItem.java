package com.jmail.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {

    private String name;
    private ObservableList<EmailMessage> emailMessages;
    private int unreadMessagesCount;

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }

    public void addEmail(Message message) throws MessagingException {
        boolean messageIsRead = message.getFlags().contains(Flags.Flag.SEEN);
        final var emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageIsRead,
                message
        );
        emailMessages.add(emailMessage);
        if (!messageIsRead)
            incrementMessageCount();

        System.out.println("Added to " + name + " " + message.getSubject());
    }

    public void incrementMessageCount() {
        unreadMessagesCount++;
        updateName();
    }

    private void updateName() {
        if (unreadMessagesCount > 0)
            this.setValue((String) (name + "(" + unreadMessagesCount + ")"));
        else
            this.setValue(name);
    }

    public ObservableList<EmailMessage> getEmailMessages() {
        return this.emailMessages;
    }
}
