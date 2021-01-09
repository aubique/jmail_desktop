package com.jmail.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "validAccounts.ser";

    public List<ValidAccount> loadFromPersistence() {
        List<ValidAccount> resultList = new ArrayList<ValidAccount>();
        try {
            final var fileInputSystem = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            final var objectInputStream = new ObjectInputStream(fileInputSystem);
            final List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public void saveToPersistance(List<ValidAccount> validAccounts) {
        try {
            final var file = new File(VALID_ACCOUNTS_LOCATION);
            final var fileOutputStream = new FileOutputStream(file);
            final var objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
