package com.googlecode.propidle.server;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.utterlyidle.httpserver.RestServer;

import java.util.concurrent.Callable;

public class Server {
    public static void main(String[] args) throws Exception {
        TestPropertiesApplication application = new TestPropertiesApplication();
        application.inTransaction(CreateTestUser.class);

        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new RestServer(port, basePath("/"), application);
    }

    public static class CreateTestUser implements Callable<User> {
        private final Users users;
        private final PasswordHasher passwordHasher;
        private final Username username = username("bob");
        private final Password password = password("iambob");

        public CreateTestUser(Users users, PasswordHasher passwordHasher) {
            this.users = users;
            this.passwordHasher = passwordHasher;
        }

        public User call() throws Exception {
            User user = user(username, passwordHasher.hash(password));
            users.put(user);
            System.out.println(String.format("Created user %s with password %s", username, password));
            return user;
        }
    }
}
