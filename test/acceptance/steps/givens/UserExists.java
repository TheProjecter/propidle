package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.User.user;
import acceptance.Values;

import java.util.concurrent.Callable;

public class UserExists implements Callable<User> {
    private final Users users;
    private final PasswordHasher passwordHasher;

    private Username username;
    private Password password;


    public UserExists(Users users, PasswordHasher passwordHasher) {
        this.users = users;
        this.passwordHasher = passwordHasher;
    }

    public User call() throws Exception {
        User user = user(username, passwordHasher.hash(password));
        users.put(user);
        return user;
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
