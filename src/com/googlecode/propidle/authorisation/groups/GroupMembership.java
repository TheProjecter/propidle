package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.authorisation.users.Username;

public class GroupMembership {
    private final Username username;
    private final GroupId groupId;

    public static GroupMembership groupMembership(Username username, GroupId groupId) {
        return new GroupMembership(username, groupId);
    }

    private GroupMembership(Username username, GroupId groupId) {
        this.username = username;
        this.groupId = groupId;
    }

    public Username username() {
        return username;
    }

    public GroupId groupId() {
        return groupId;
    }
}
