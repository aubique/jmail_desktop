package com.jmail;

import com.jmail.model.EmailAccount;
import javafx.scene.control.TreeItem;

public class EmailManager {

    //Folder handling:
    private TreeItem<String> folderRoot = new TreeItem<>("");

    public TreeItem<String> getFolderRoot() {
        return folderRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        final var treeItem = new TreeItem<String>(emailAccount.getAddress());
        treeItem.setExpanded(true);
        treeItem.getChildren().add(new TreeItem<String>("Inbox"));
        treeItem.getChildren().add(new TreeItem<String>("Sent"));
        treeItem.getChildren().add(new TreeItem<String>("Temp"));
        treeItem.getChildren().add(new TreeItem<String>("Spam"));
        folderRoot.getChildren().add(treeItem);
    }
}
