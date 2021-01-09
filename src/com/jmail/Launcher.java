package com.jmail;

import com.jmail.controller.persistence.PersistenceAccess;
import com.jmail.controller.persistence.ValidAccount;
import com.jmail.controller.services.LoginService;
import com.jmail.model.EmailAccount;
import com.jmail.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private final PersistenceAccess persistenceAccess = new PersistenceAccess();
    private final EmailManager emailManager = new EmailManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final var viewFactory = new ViewFactory(emailManager);
        final List<ValidAccount> validAccountsList = persistenceAccess.loadFromPersistence();
        if (validAccountsList.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount : validAccountsList) {
                final var emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                final var loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else
            viewFactory.showLoginWindow();
    }

    @Override
    public void stop() throws Exception {
        final List<ValidAccount> validAccountList = new ArrayList<ValidAccount>();
        for (EmailAccount emailAccount : emailManager.getEmailAccounts())
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        persistenceAccess.saveToPersistance(validAccountList);
    }
}
