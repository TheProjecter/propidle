package com.googlecode.propidle.authorisation.users;

import static com.googlecode.propidle.authorisation.users.PasswordHash.passwordHash;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authorisation.users.UsersFromRecords.defineUsersRecord;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.totallylazy.Option.none;
import com.googlecode.totallylazy.Option;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class UsersFromRecordsTest {
    private final UsersFromRecords users = new UsersFromRecords(defineUsersRecord(temporaryRecords()));

    @Test
    public void shouldReplaceExistingUserWithSameName() throws Exception {
        users.put(user(
                username("donald trump"),
                passwordHash("stringy hairpiece")));

        users.put(user(
                username("donald trump"),
                passwordHash("fluffy smooshbag")));

        assertThat(users.get(username("donald trump")).get().passwordHash(), is(passwordHash("fluffy smooshbag")));
    }

    @Test
    public void shouldBeAbleToRemoveAUser() throws Exception {
        users.put(user(
                username("steve tyler"),
                passwordHash("blubbery")));

        assertThat(users.remove(username("steve tyler")).isEmpty(), is(false));

        assertThat(users.get(username("steve tyler")), is((Option)none()));

        assertThat(users.remove(username("steve tyler")), is((Option)none()));
    }
}
