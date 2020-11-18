package com.jmail.model;

import javax.mail.Store;
import java.util.Properties;

public class EmailAccount {

    private final String address;
    private final String password;
    private Properties properties;
    private Store store;

    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        this.properties = new Properties();

        this.properties.put("incomingHost", "imap.gmail.com");
        this.properties.put("mail.store.protocol", "imaps");
        this.properties.put("mail.transport.protocol", "smtps");
        this.properties.put("mail.smtps.host", "smtp.gmail.com");
        this.properties.put("mail.smtps.auth", "true");
        this.properties.put("outgoingHost", "smtp.gmail.com");
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    @Deprecated
    public char[] getPasswordAsCharArray() {
        return password.toCharArray();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
