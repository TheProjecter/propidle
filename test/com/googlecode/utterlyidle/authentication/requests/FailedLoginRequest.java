package com.googlecode.utterlyidle.authentication.requests;

import com.googlecode.utterlyidle.authentication.Base64RequestEncoding;
import com.googlecode.utterlyidle.authentication.application.TestApplication;

public class FailedLoginRequest extends PostFromLoginForm {
    public FailedLoginRequest(TestApplication testApplication, final Base64RequestEncoding base64RequestEncoding, final ActionUrl actionUrl) {
        super(testApplication, "rubert", "murdock", base64RequestEncoding, actionUrl);
    }
}
