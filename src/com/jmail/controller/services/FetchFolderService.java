package com.jmail.controller.services;

import com.jmail.model.EmailTreeItem;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

public class FetchFolderService extends Service<Void> {

    private final Store store;
    private final EmailTreeItem<String> folderRoot;
    private final List<Folder> folderList;

    public FetchFolderService(Store store, EmailTreeItem<String> folderRoot, List<Folder> folderList) {
        this.store = store;
        this.folderRoot = folderRoot;
        this.folderList = folderList;
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
        handleFolders(folders, folderRoot);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> folderRoot)
            throws MessagingException {
        for (Folder folder : folders) {
            folderList.add(folder);
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<>(folder.getName());
            folderRoot.getChildren().add(emailTreeItem);
            folderRoot.setExpanded(true);
            fetchMessagesInFolder(folder, emailTreeItem);
            addMessageListenerToFolder(folder, emailTreeItem);

            if (folder.getType() == Folder.HOLDS_FOLDERS) {
                final Folder[] subFolders = folder.list();
                handleFolders(subFolders, emailTreeItem);
            }
        }
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent event) {
                System.out.println("Event: message ADDED - " + event);//FIXME delsout
                for (int i = 0; i < event.getMessages().length; i++) {
                    try {
                        final Message msg = folder.getMessage(folder.getMessageCount() - i);
                        emailTreeItem.addEmailToTop(msg);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent messageCountEvent) {
                System.out.println("Event: message REMOVE - " + messageCountEvent);
            }
        });
    }

    private void fetchMessagesInFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
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
                                System.out.println(folder.getMessage(i).getSubject());//TODO remove stdout
                                emailTreeItem.addEmail(folder.getMessage(i));
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
