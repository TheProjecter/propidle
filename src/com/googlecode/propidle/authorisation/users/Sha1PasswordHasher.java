package com.googlecode.propidle.authorisation.users;

import static com.googlecode.propidle.util.Sha1.sha1;

public class Sha1PasswordHasher implements PasswordHasher{
    private final PasswordSalt salt;

    public Sha1PasswordHasher(PasswordSalt salt) {
        this.salt = salt;
    }

    public PasswordHash hash(Password password) {
        return new PasswordHash(sha1(password.value() + salt.value()));
    }
}
