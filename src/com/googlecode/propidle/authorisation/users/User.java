package com.googlecode.propidle.authorisation.users;

public class User {
    private final Username username;
    private final PasswordHash passwordHash;

    public static User user(Username username, PasswordHash passwordHash) {
        return new User(username, passwordHash);
    }

    protected User(Username username, PasswordHash passwordHash) {
        this.passwordHash = passwordHash;
        this.username = username;
    }

    public Username username() {
        return username;
    }

    public PasswordHash passwordHash() {
        return passwordHash;
    }
}
