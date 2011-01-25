package com.googlecode.propidle.authorisation.users;

import com.googlecode.totallylazy.Option;

public interface Users {
    User get(Username username);
    Users put(User user);
    Option<User> remove(Username username);
}
