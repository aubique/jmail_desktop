package com.jmail;

import com.jmail.controller.services.FetchFolderService;
import com.jmail.controller.services.FolderUpdaterService;
import com.jmail.model.EmailAccount;
import com.jmail.model.EmailMessage;
import com.jmail.model.EmailTreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    //Folder handling:
    private final EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");
    private final FolderUpdaterService folderUpdaterService;
    private final List<Folder> folderList = new ArrayList<>();
    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public ObservableList<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public EmailTreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public List<Folder> getFolderList() {
        return this.folderList;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccounts.add(emailAccount);
        final EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        final var fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();

        folderRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUnread() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessageCount();
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
