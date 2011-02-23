package com.googlecode.propidle.authentication;

import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authentication.Session.session;
import static com.googlecode.propidle.authentication.SessionId.sessionId;
import static com.googlecode.propidle.authentication.StartTime.startTime;
import com.googlecode.propidle.Coercions;
import static com.googlecode.propidle.Coercions.coerce;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.util.Date;
import java.sql.Timestamp;

public class SessionsFromRecords implements Sessions {
    private static final Keyword SESSIONS = keyword("sessions");
    private static final Keyword<String> SESSION_ID = keyword("session_id", String.class);
    private static final Keyword<String> SESSION_USERNAME = keyword("session_username", String.class);
    private static final Keyword<Object> SESSION_START_TIME = keyword("session_start_time", Object.class);

    private final Records records;

    public SessionsFromRecords(Records records) {
        this.records = records;
    }

    public Sessions put(Session session) {
        records.remove(SESSIONS, (where(SESSION_ID, is(session.id().value()))));
        records.add(SESSIONS, serialise(session));
        return this;
    }

    public Option<Session> get(SessionId id) {
        Option<Record> record = records.get(SESSIONS).filter(where(SESSION_ID, is(id.value().toString()))).headOption();

        if (record.isEmpty()) {
            return none(Session.class);
        } else {
            return some(deserialise(record.get()));
        }
    }

    private Session deserialise(Record record) {
        return session(
                sessionId(record.get(SESSION_ID)),
                username(record.get(SESSION_USERNAME)),
                startTime(coerce(record.get(SESSION_START_TIME), Date.class)));
    }

    private Record serialise(Session session) {
        return record().
                set(SESSION_ID, session.id().value().toString()).
                set(SESSION_USERNAME, session.username().toString()).
                set(SESSION_START_TIME, new Timestamp(session.startTime().value().getTime()));
    }

    public static Records defineSessionsRecord(Records records) {
        records.define(SESSIONS, SESSION_ID, SESSION_USERNAME, SESSION_START_TIME);
        return records;
    }
}
