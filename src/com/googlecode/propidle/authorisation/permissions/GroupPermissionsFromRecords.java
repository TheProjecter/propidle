package com.googlecode.propidle.authorisation.permissions;

import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.GroupId.groupId;
import static com.googlecode.propidle.authorisation.groups.GroupsFromRecords.GROUP_ID;
import static com.googlecode.propidle.authorisation.permissions.GroupPermission.groupPermission;
import static com.googlecode.propidle.authorisation.permissions.GroupPermission.groupPermissions;
import static com.googlecode.propidle.authorisation.permissions.Permission.permission;
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

public class GroupPermissionsFromRecords implements GroupPermissions {
    private static final Keyword GROUP_PERMISSIONS = keyword("group_permissions");
    private static final Keyword<String> PERMISSION = keyword("permission", String.class);

    private final Records records;

    public GroupPermissionsFromRecords(Records records) {
        this.records = records;
    }

    public Iterable<GroupPermission> get(GroupId groupId) {
        return deserialise(records.get(GROUP_PERMISSIONS).filter(where(GROUP_ID, is(groupId.toString()))));
    }

    public Iterable<GroupPermission> grant(GroupId groupId, Iterable<Permission> permissions) {
        Iterable<GroupPermission> groupPermissions = get(groupId);
        Sequence<Permission> existingPermissions = sequence(groupPermissions).map(getPermission());
        Set<Permission> uniquePermissions = sequence(permissions).toSet();
        Sequence<Permission> newPermissions = sequence(uniquePermissions).filter(not(in(existingPermissions)));

        records.add(GROUP_PERMISSIONS, serialise(groupPermissions(groupId, newPermissions)));

        return sequence(groupPermissions).join(groupPermissions(groupId, newPermissions));
    }

    public Iterable<GroupPermission> revoke(GroupId groupId, Iterable<Permission> permissions) {
        records.remove(GROUP_PERMISSIONS,
                       where(GROUP_ID, is(groupId.toString())).
                               and(where(PERMISSION, in(sequence(permissions).map(asString())))));
        return get(groupId);
    }

    private Sequence<Record> serialise(Iterable<GroupPermission> permissions) {
        return sequence(permissions).map(serialise());
    }

    private Callable1<GroupPermission, Record> serialise() {
        return new Callable1<GroupPermission, Record>() {
            public Record call(GroupPermission permission) throws Exception {
                return record().
                        set(GROUP_ID, permission.groupId().toString()).
                        set(PERMISSION, permission.permission().value());
            }
        };
    }

    private Iterable<GroupPermission> deserialise(Sequence<Record> records) {
        return records.map(deserialise());
    }

    private Callable1<? super Record, GroupPermission> deserialise() {
        return new Callable1<Record, GroupPermission>() {
            public GroupPermission call(Record record) throws Exception {
                return groupPermission(groupId(record.get(GROUP_ID)), permission(record.get(PERMISSION)));
            }
        };
    }

    public static Records defineGroupPermissionsRecord(Records records) {
        records.define(GROUP_PERMISSIONS, GROUP_ID, PERMISSION);
        return records;
    }

    public Callable1<GroupPermission, Permission> getPermission() {
        return new Callable1<GroupPermission, Permission>() {
            public Permission call(GroupPermission groupPermission) throws Exception {
                return groupPermission.permission();
            }
        };
    }
}
