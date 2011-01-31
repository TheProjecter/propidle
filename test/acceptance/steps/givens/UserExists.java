package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.User.user;
import acceptance.Values;
import acceptance.steps.Given;

import java.util.concurrent.Callable;

public class UserExists implements Callable<User> {
    private final Username username;
    private final Password password;

    private final Users users;
    private final PasswordHasher passwordHasher;

    public UserExists(Username username, Password password, Users users, PasswordHasher passwordHasher) {
        this.username = username;
        this.password = password;
        this.users = users;
        this.passwordHasher = passwordHasher;
    }

    public User call() throws Exception {
        User user = user(username, passwordHasher.hash(password));
        users.put(user);
        return user;
    }

    public static Given<User> userExists(Values usingValues) {
        return new Given<User>(UserExists.class, usingValues);
    }
}
