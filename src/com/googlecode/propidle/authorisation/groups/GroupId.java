package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.util.UuidTinyType;

import java.util.UUID;

public class GroupId extends UuidTinyType<GroupId>{
    public static GroupId groupId() {
        return groupId(UUID.randomUUID());
    }
    public static GroupId groupId(UUID value) {
        return new GroupId(value);
    }

    protected GroupId(UUID value) {
        super(value);
    }
}
