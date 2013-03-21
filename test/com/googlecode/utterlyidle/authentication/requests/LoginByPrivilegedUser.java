package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.utterlyidle.authentication.Base64RequestEncoding;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

public class LoginByPrivilegedUser extends PostFromLoginForm {

    public LoginByPrivilegedUser(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "admin", "admin", base64RequestEncoding, actionUrl);
    }

}
