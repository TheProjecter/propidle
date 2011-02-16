package com.googlecode.propidle.authorisation.users;

import static com.googlecode.propidle.authorisation.users.PasswordHash.passwordHash;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.propidle.authorisation.users.Username.username;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.math.BigInteger;

public class UsersFromRecords implements Users {
    public static final Keyword USERS = keyword("users");
    public static final Keyword<String> USERNAME = keyword("username", String.class);
    public static final Keyword<Number> PASSWORD_HASH = keyword("password_hash", Number.class);

    private final Records records;

    public UsersFromRecords(Records records) {
        this.records = records;
    }

    public Option<User> get(Username username) {
        return records.get(USERS).filter(where(USERNAME, is(username.value()))).headOption().map(deserialise());
    }

    public User put(User user) {
        remove(user.username());
        records.add(USERS, serialise(user));
        return user;
    }

    public Option<User> remove(Username username) {
        Option<User> existing = get(username);
        records.remove(USERS, where(USERNAME, is(username.value())));
        return existing;
    }

    private Record serialise(User user) {
        return record().
                set(USERNAME, user.username().value()).
                set(PASSWORD_HASH, user.passwordHash().value());
    }

    private Callable1<Record, User> deserialise() {
        return new Callable1<Record, User>() {
            public User call(Record record) throws Exception {
                return user(
                        username(record.get(USERNAME)),
                        passwordHash(record.get(PASSWORD_HASH)));
            }
        };
    }

    public static Records defineUsersRecord(Records records) {
        records.define(USERS, USERNAME, PASSWORD_HASH);
        return records;
    }
}
