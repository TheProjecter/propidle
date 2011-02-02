package com.googlecode.propidle.authorisation.users;

import static com.googlecode.propidle.authorisation.users.PasswordHash.passwordHash;
import static com.googlecode.propidle.authorisation.users.User.user;
import static com.googlecode.propidle.authorisation.users.Username.username;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

public class UsersFromRecords implements Users {
    private static final Keyword USERS = keyword("users");
    private static final Keyword<String> USERNAME = keyword("username", String.class);
    private static final Keyword<String> PASSWORD_HASH = keyword("password_hash", String.class);

    private final Records records;

    public UsersFromRecords(Records records) {
        this.records = records;
    }

    public User get(Username username) {
        Option<Record> record = records.get(USERS).filter(where(USERNAME, is(username.value()))).headOption();
        if (record.isEmpty()) {
            return null;
        } else {
            return deserialise(record.get());
        }
    }

    public Users put(User user) {
        remove(user.username());
        records.add(USERS, serialise(user));
        return this;
    }

    public Option<User> remove(Username username) {
        User existing = get(username);
        records.remove(USERS, where(USERNAME, is(username.value())));
        return some(existing);
    }

    private Record serialise(User user) {
        return record().
                set(USERNAME, user.username().value()).
                set(PASSWORD_HASH, user.passwordHash().value());
    }

    private User deserialise(Record record) {
        return user(
                username(record.get(USERNAME)),
                passwordHash(record.get(PASSWORD_HASH)));
    }

    public static Records defineUsersRecord(Records records) {
        records.define(USERS, USERNAME, PASSWORD_HASH);
        return records;
    }
}
