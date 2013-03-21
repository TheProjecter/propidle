package com.googlecode.propidle.authentication.requests;

import com.googlecode.propidle.authentication.Base64RequestEncoding;
import com.googlecode.propidle.authentication.application.TestApplication;

public class LoginByUnprivilegedUser extends PostFromLoginForm {

    public LoginByUnprivilegedUser(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "fred", "fred", base64RequestEncoding, actionUrl);
    }

}
