package com.jmail;

import com.jmail.controller.services.FetchFolderService;
import com.jmail.model.EmailAccount;
import com.jmail.model.EmailTreeItem;

public class EmailManager {

    //Folder handling:
    private final EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");

    public EmailTreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        final EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        final var fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem);
        fetchFolderService.start();

        folderRoot.getChildren().add(treeItem);
    }
}
