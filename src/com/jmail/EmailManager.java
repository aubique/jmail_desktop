package com.jmail;

import com.jmail.controller.services.FetchFolderService;
import com.jmail.controller.services.FolderUpdaterService;
import com.jmail.model.EmailAccount;
import com.jmail.model.EmailMessage;
import com.jmail.model.EmailTreeItem;

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

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
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
        }
    }
}
