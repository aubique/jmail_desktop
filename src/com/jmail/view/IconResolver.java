package com.jmail.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconResolver {

    public Node getIconForFolder(String folderName) {
        String lowerCaseFolder = folderName.toLowerCase();
        final ImageView imageView;

        try {
            if (lowerCaseFolder.contains("@"))
                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/email.png")));
            else if (lowerCaseFolder.contains("inbox"))
                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/inbox.png")));
            else if (lowerCaseFolder.contains("sent"))
                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/sent2.png")));
            else if (lowerCaseFolder.contains("spam"))
                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/spam.png")));
            else
                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/folder.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return imageView;
    }
}

