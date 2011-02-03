package acceptance.steps.givens;

import acceptance.Values;
import com.googlecode.propidle.authorisation.users.Users;
import com.googlecode.propidle.authorisation.users.User;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.totallylazy.Option;

import java.util.concurrent.Callable;

public class UserDoesNotExist implements Callable<Option<User>> {
    private final Users users;
    private Username username;

    public UserDoesNotExist(Users users) {
        this.users = users;
    }

    public UserDoesNotExist with(Username username) {
        this.username = username;
        return this;
    }

    public Option<User> call() throws Exception {
        return users.remove(username);
    }
}
