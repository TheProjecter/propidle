package com.googlecode.propidle.authorisation.users;

import static com.googlecode.propidle.authorisation.users.PasswordHash.passwordHash;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.none;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import java.math.BigInteger;

public class UsersFromRecordsTest {
    private final UsersFromRecords users = new UsersFromRecords(testRecordsWithAllMigrationsRun());

    @Test
    public void shouldReplaceExistingUserWithSameName() throws Exception {
        users.put(user(
                username("donald trump"),
                passwordHash(new BigInteger("1"))));

        users.put(user(
                username("donald trump"),
                passwordHash(new BigInteger("2"))));

        assertThat(users.get(username("donald trump")).get().passwordHash(), is(passwordHash(new BigInteger("2"))));
    }

    @Test
    public void shouldBeAbleToRemoveAUser() throws Exception {
        users.put(user(
                username("steve tyler"),
                passwordHash(new BigInteger("1"))));

        assertThat(users.remove(username("steve tyler")).isEmpty(), is(false));

        assertThat(users.get(username("steve tyler")), is((Option) none()));

        assertThat(users.remove(username("steve tyler")), is((Option) none()));
    }
}
