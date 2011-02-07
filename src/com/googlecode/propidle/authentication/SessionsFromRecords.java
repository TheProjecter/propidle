package com.googlecode.propidle.authentication;

import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authentication.Session.session;
import static com.googlecode.propidle.authentication.SessionId.sessionId;
import static com.googlecode.propidle.authentication.StartTime.startTime;
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

public class SessionsFromRecords implements Sessions {
    private static final Keyword SESSIONS = keyword("sessions");
    private static final Keyword<String> SESSION_ID = keyword("session_id", String.class);
    private static final Keyword<String> USERNAME = keyword("username", String.class);
    private static final Keyword<Date> START_TIME = keyword("starttime", Date.class);

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
                username(record.get(USERNAME)),
                StartTime.startTime(record.get(START_TIME)));
    }

    private Record serialise(Session session) {
        return record().
                set(SESSION_ID, session.id().value().toString()).
                set(USERNAME, session.username().toString()).
                set(START_TIME, session.startTime().value());
    }

    public static Records defineSessionsRecord(Records records) {
        records.define(SESSIONS, SESSION_ID, USERNAME, START_TIME);
        return records;
    }
}
