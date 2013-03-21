package com.googlecode.propidle.authentication.requests;

import com.googlecode.propidle.authentication.Base64RequestEncoding;
import com.googlecode.propidle.authentication.application.TestApplication;

public class FailedLoginRequest extends PostFromLoginForm {
    public FailedLoginRequest(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "rubert", "murdock", base64RequestEncoding, actionUrl);
    }
}
