package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.httpserver.RestServer;

import java.io.IOException;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.yadic.Container;
import com.googlecode.totallylazy.Callable1;

public class Server {
    public static void main(String[] args) throws IOException {
        TestPropertiesApplication application = new TestPropertiesApplication();
        application.inTransaction(createTestUser(username("admin"), password("hogtied")));

        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new RestServer(port, basePath("/"), application);
    }

    private static Callable1<Container, User> createTestUser(final Username username, final Password password) {
        return new Callable1<Container, User>() {
            public User call(Container container) throws Exception {
                PasswordHasher passwordHasher = container.get(PasswordHasher.class);
                User user = user(username, passwordHasher.hash(password));
                container.get(Users.class).put(user);
                System.out.println(String.format("Created user %s with password %s", username, password));
                return user;
            }
        };
    }
}
