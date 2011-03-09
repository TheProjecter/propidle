package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.util.tinytype.UuidTinyType;
import com.googlecode.totallylazy.Callable1;

import java.util.UUID;

public class GroupId extends UuidTinyType<GroupId>{
    public static GroupId newGroupId() {
        return groupId(UUID.randomUUID());
    }
    public static GroupId groupId(String value) {
        return groupId(UUID.fromString(value));
    }
    public static GroupId groupId(UUID value) {
        return new GroupId(value);
    }

    protected GroupId(UUID value) {
        super(value);
    }

    public static Callable1<? super String, GroupId> asGroupId() {
        return new Callable1<String, GroupId>() {
            public GroupId call(String value) throws Exception {
               return groupId(value);
            }
        };
    }
}
