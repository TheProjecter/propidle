package com.googlecode.propidle.authentication;

import com.googlecode.propidle.util.HumanReadable;
import static com.googlecode.propidle.util.HumanReadable.humanReadable;
import com.googlecode.propidle.authorisation.users.Username;

public class AuthenticationException extends Exception {
    private final Username username;
    private HumanReadable message;

    public AuthenticationException(Username username) {
        this(username, (Throwable) null);
    }

    public AuthenticationException(Username username, Throwable cause) {
        this(username, defaultMessage(username), cause);
    }

    public AuthenticationException(Username username, HumanReadable message, Throwable cause) {
        super(message.value(), cause);
        this.username = username;
        this.message = message;
    }

    private static HumanReadable defaultMessage(Username username) {
        return humanReadable("Could not authenticate '%s' with the provided credentials", username);
    }

    public Username username() {
        return username;
    }

    public HumanReadable userMessage() {
        return message;
    }
}
