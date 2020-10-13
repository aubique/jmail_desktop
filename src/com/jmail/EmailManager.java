package com.jmail;

import com.jmail.controller.services.FetchFolderService;
import com.jmail.model.EmailAccount;
import com.jmail.model.EmailTreeItem;
import javafx.scene.control.TreeItem;

public class EmailManager {

    //Folder handling:
    private EmailTreeItem<String> folderRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        final var fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem);
        fetchFolderService.start();

        folderRoot.getChildren().add(treeItem);
    }
}
