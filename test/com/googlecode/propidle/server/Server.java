package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.httpserver.RestServer;

import java.io.IOException;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.authorisation.users.User;
import com.googlecode.propidle.authorisation.users.Users;
import com.googlecode.propidle.authorisation.users.PasswordHasher;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.yadic.Container;
import com.googlecode.totallylazy.Callable1;

public class Server {
    public static void main(String[] args) throws IOException {
        TestPropertiesApplication application = new TestPropertiesApplication();
        application.defineRecords();
        application.inTransaction(createTestUser());

        int port = args.length > 0 ? Integer.valueOf(args[0]) : 8000;
        new RestServer(port, basePath("/"), application);
    }

    private static Callable1<Container, User> createTestUser() {
        return new Callable1<Container, User>() {
            public User call(Container container) throws Exception {
                PasswordHasher passwordHasher = container.get(PasswordHasher.class);
                User user = user(username("admin"), passwordHasher.hash(password("hogtied")));
                container.get(Users.class).put(user);
                return user;
            }
        };
    }
}
