package com.googlecode.propidle.authorisation.groups;

import com.googlecode.totallylazy.Option;

public interface Groups {
    Iterable<Group> get(Iterable<GroupId> ids);
    Option<Group> get(GroupName name);
    Iterable<Group> get();
    Group add(Group group);
}
