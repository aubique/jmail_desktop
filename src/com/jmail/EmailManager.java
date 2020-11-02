package com.jmail;

import com.jmail.controller.services.FetchFolderService;
import com.jmail.controller.services.FolderUpdaterService;
import com.jmail.model.EmailAccount;
import com.jmail.model.EmailTreeItem;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    //Folder handling:
    private final EmailTreeItem<String> folderRoot = new EmailTreeItem<>("");
    private FolderUpdaterService folderUpdaterService;
    private List<Folder> folderList = new ArrayList<Folder>();

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
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
}
