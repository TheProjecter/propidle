package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.utterlyidle.authentication.Base64RequestEncoding;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

public class LoginByUnprivilegedUser extends PostFromLoginForm {

    public LoginByUnprivilegedUser(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "fred", "fred", base64RequestEncoding, actionUrl);
    }

}
