package com.googlecode.propidle.authorisation.groups;

import static com.googlecode.propidle.authorisation.groups.Group.group;
import static com.googlecode.propidle.authorisation.groups.GroupId.groupId;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.records.Keyword;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

public class GroupsFromRecords implements Groups {
    private final Records records;
    public static final Keyword GROUPS = keyword("groups");
    public static final Keyword<String> GROUP_ID = keyword("group_id", String.class);
    public static final Keyword<String> GROUP_NAME = keyword("group_name", String.class);

    public GroupsFromRecords(Records records) {
        this.records = records;
    }

    public Iterable<Group> get() {
        return records.get(GROUPS).map(deserialise()).realise();
    }

    public Iterable<Group> get(Iterable<GroupId> ids) {
        return records.get(GROUPS).filter(where(GROUP_ID, in(sequence(ids).map(asString())))).map(deserialise()).realise();
    }

    public Option<Group> get(GroupName name) {
        return records.get(GROUPS).filter(where(GROUP_NAME, is(name.value()))).realise().headOption().map(deserialise());
    }

    public Group add(Group group) {
        records.remove(GROUPS, where(GROUP_ID, is(group.id().toString())));
        records.add(GROUPS, serialise(group));
        return group;
    }

    private Callable1<? super Record, Group> deserialise() {
        return new Callable1<Record, Group>() {
            public Group call(Record record) throws Exception {
                return group(
                        groupId(record.get(GROUP_ID)),
                        groupName(record.get(GROUP_NAME)));
            }
        };
    }

    private Record serialise(Group group) {
        return record().
                set(GROUP_ID, group.id().value().toString()).
                set(GROUP_NAME, group.name().value());
    }

    public static Records defineGroupsRecord(Records records) {
        records.define(GROUPS, GROUP_ID, GROUP_NAME);
        return records;
    }
}
