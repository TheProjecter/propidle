package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.authorisation.users.Username;

public interface GroupMemberships {
    Iterable<GroupMembership> add(Username username, Iterable<GroupId> groups);
    Iterable<GroupMembership> remove(Username username, Iterable<GroupId> groups);
    Iterable<GroupMembership> get(Username username);
}
