package com.googlecode.propidle.server.steps.givens;

import com.googlecode.propidle.server.steps.Given;
import com.googlecode.propidle.server.Values;
import com.googlecode.propidle.authorisation.users.Users;
import com.googlecode.propidle.authorisation.users.User;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.totallylazy.Option;

import java.util.concurrent.Callable;

public class UserDoesNotExist implements Callable<Option<User>> {
    private final Username username;
    private final Users users;

    public UserDoesNotExist(Username username, Users users) {
        this.username = username;
        this.users = users;
    }

    public static Given<Option<User>> userDoesNotExist(final Values values){
        return new Given<Option<User>>(UserDoesNotExist.class, values);
    }

    public Option<User> call() throws Exception {
        return users.remove(username);
    }
}
