package com.googlecode.propidle.authorisation.users;

public interface PasswordHasher {
    PasswordHash hash(Password password);
}
