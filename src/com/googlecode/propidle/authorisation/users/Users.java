package com.googlecode.propidle.authorisation.users;

import com.googlecode.totallylazy.Option;

public interface Users {
    Option<User> get(Username username);
    User put(User user);
    Option<User> remove(Username username);
}
