package com.googlecode.propidle.authorisation.groups;

import static com.googlecode.propidle.authorisation.groups.GroupId.groupId;
import static com.googlecode.propidle.authorisation.groups.GroupMembership.groupMembership;
import static com.googlecode.propidle.authorisation.groups.GroupMembership.groupMemberships;
import static com.googlecode.propidle.authorisation.groups.GroupsFromRecords.GROUP_ID;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.authorisation.users.UsersFromRecords.USERNAME;
import com.googlecode.totallylazy.Callable1;
import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.*;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.util.Set;

public class GroupMembershipsFromRecords implements GroupMemberships {
    public static Keyword GROUP_MEMBERSHIPS = keyword("group_memberships");
    private final Records records;

    public GroupMembershipsFromRecords(Records records) {
        this.records = records;
    }

    public Iterable<GroupMembership> get(Username username) {
        return records.get(GROUP_MEMBERSHIPS).filter(where(USERNAME, is(username.value()))).map(deserialise()).realise();
    }

    public Iterable<GroupMembership> add(Username username, Iterable<GroupId> groups) {
        Sequence<GroupMembership> memberships = sequence(get(username));
        Sequence<GroupId> existingGroups = sequence(memberships).map(getGroupId());
        Set<GroupId> newGroups = sequence(groups).filter(not(in(existingGroups))).toSet();
        records.add(GROUP_MEMBERSHIPS, serialise(groupMemberships(username, newGroups)));
        return memberships.join(groupMemberships(username, newGroups));
    }

    public Iterable<GroupMembership> remove(Username username, Iterable<GroupId> groups) {
        records.remove(GROUP_MEMBERSHIPS, where(USERNAME, is(username.value())).and(where(GROUP_ID, in(sequence(groups).map(asString())))));
        return get(username);
    }

    private Callable1<? super Record, GroupMembership> deserialise() {
        return new Callable1<Record, GroupMembership>() {
            public GroupMembership call(Record record) throws Exception {
                return groupMembership(username(record.get(USERNAME)), groupId(record.get(GROUP_ID)));
            }
        };
    }

    private Sequence<Record> serialise(Iterable<GroupMembership> memberships) {
        return sequence(memberships).map(serialise());
    }

    private Callable1<? super GroupMembership, Record> serialise() {
        return new Callable1<GroupMembership, Record>() {
            public Record call(GroupMembership membership) throws Exception {
                return record().
                        set(GROUP_ID, membership.groupId().toString()).
                        set(USERNAME, membership.username().value());
            }
        };
    }

    private Callable1<? super GroupMembership, GroupId> getGroupId() {
        return new Callable1<GroupMembership, GroupId>() {
            public GroupId call(GroupMembership groupMembership) throws Exception {
                return groupMembership.groupId();
            }
        };
    }

    public static Records defineGroupMembershipsRecord(Records records) {
        records.define(GROUP_MEMBERSHIPS, GROUP_ID, USERNAME);
        return records;
    }
}
