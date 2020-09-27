package com.jmail.controller.services;

import com.jmail.EmailManager;
import com.jmail.controller.EmailLoginResult;
import com.jmail.model.EmailAccount;

public class LoginService {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    public EmailLoginResult login() {
        return null;
    }
}
