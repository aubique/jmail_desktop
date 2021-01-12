package com.jmail.controller.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceAccess {

    private String VALID_ACCOUNTS_LOCATION = System.getProperty("user.home") + File.separator + "validAccounts.ser";
    private Encoder encoder = new Encoder();

    public List<ValidAccount> loadFromPersistence() {
        List<ValidAccount> resultList = new ArrayList<ValidAccount>();
        try {
            final var fileInputSystem = new FileInputStream(VALID_ACCOUNTS_LOCATION);
            final var objectInputStream = new ObjectInputStream(fileInputSystem);
            final List<ValidAccount> persistedList = (List<ValidAccount>) objectInputStream.readObject();
            decodePasswords(persistedList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private void decodePasswords(List<ValidAccount> persistedList) {
        for (var validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.decode(originalPassword));
        }
    }

    private void encodePasswords(List<ValidAccount> persistedList) {
        for (var validAccount : persistedList) {
            String originalPassword = validAccount.getPassword();
            validAccount.setPassword(encoder.encode(originalPassword));
        }
    }

    public void saveToPersistance(List<ValidAccount> validAccounts) {
        try {
            final var file = new File(VALID_ACCOUNTS_LOCATION);
            final var fileOutputStream = new FileOutputStream(file);
            final var objectOutputStream = new ObjectOutputStream(fileOutputStream);

            encodePasswords(validAccounts);
            objectOutputStream.writeObject(validAccounts);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
