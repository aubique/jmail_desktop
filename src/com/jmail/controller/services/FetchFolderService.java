package com.jmail.controller.services;

import com.jmail.model.EmailTreeItem;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

public class FetchFolderService extends Service<Void> {

    private final Store store;
    private final EmailTreeItem<String> folderRoot;

    public FetchFolderService(Store store, EmailTreeItem<String> folderRoot) {
        this.store = store;
        this.folderRoot = folderRoot;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolder(folders, folderRoot);
    }

    private void handleFolder(Folder[] folders, EmailTreeItem<String> folderRoot)
            throws MessagingException {
        for (Folder folder : folders) {
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<>(folder.getName());
            folderRoot.getChildren().add(emailTreeItem);
            folderRoot.setExpanded(true);
            fetchMessagesFolder(folder, emailTreeItem);

            if (folder.getType() == Folder.HOLDS_FOLDERS) {
                final Folder[] subFolders = folder.list();
                handleFolder(subFolders, emailTreeItem);
            }
        }
    }

    private void fetchMessagesFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        final var fetchMessagesService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if (folder.getType() != Folder.HOLDS_FOLDERS) {
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for (int i = folderSize; i > 0; i--) {
                                System.out.println(folder.getMessage(i).getSubject());
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMessagesService.start();
    }
}
