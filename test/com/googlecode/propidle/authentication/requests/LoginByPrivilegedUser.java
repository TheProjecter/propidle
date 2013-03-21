package com.googlecode.propidle.authentication.requests;

import com.googlecode.propidle.authentication.Base64RequestEncoding;
import com.googlecode.propidle.authentication.application.TestApplication;

public class LoginByPrivilegedUser extends PostFromLoginForm {

    public LoginByPrivilegedUser(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "admin", "admin", base64RequestEncoding, actionUrl);
    }

}
