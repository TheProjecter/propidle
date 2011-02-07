package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.totallylazy.Option.option;
import acceptance.Values;

import java.util.concurrent.Callable;

public class UserExists implements Callable<User> {
    private final Users users;
    private final PasswordHasher passwordHasher;

    private Username username = username("default");
    private Password password = password("default");


    public UserExists(Users users, PasswordHasher passwordHasher) {
        this.users = users;
        this.passwordHasher = passwordHasher;
    }

    public User call() throws Exception {
        return users.get(username).getOrElse(createUser());
    }

    private Callable<User> createUser() {
        return new Callable<User>() {
            public User call() throws Exception {
                return users.put(user(username, passwordHasher.hash(password)));
            }
        };
    }

    public UserExists with(Username username) {
        this.username = username;
        return this;
    }
    public UserExists and(Password password) {
        this.password = password;
        return this;
    }
}
