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

    public void addEmail(Message msg) throws MessagingException {
        final EmailMessage emailMessage = fetchMessage(msg);
        this.emailMessages.add(emailMessage);
    }

    public void addEmailToTop(Message msg) throws MessagingException {
        final EmailMessage emailMessage = fetchMessage(msg);
        this.emailMessages.add(0, emailMessage);
    }

    private EmailMessage fetchMessage(Message msg) throws MessagingException {
        boolean messageIsRead = msg.getFlags().contains(Flags.Flag.SEEN);
        final var emailMessage = new EmailMessage(
                msg.getSubject(),
                msg.getFrom()[0].toString(),
                msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                msg.getSize(),
                msg.getSentDate(),
                messageIsRead,
                msg
        );
        if (!messageIsRead)
            incrementMessageCount();

        return emailMessage;
    }

    public void incrementMessageCount() {
        this.unreadMessagesCount++;
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
