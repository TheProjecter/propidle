package com.googlecode.propidle.authorisation.permissions;

import com.googlecode.propidle.authorisation.groups.GroupId;

public interface GroupPermissions {
    Iterable<GroupPermission> get(GroupId groupId);
    Iterable<GroupPermission> grant(GroupId groupId, Iterable<Permission> permission);
    Iterable<GroupPermission> revoke(GroupId groupId, Iterable<Permission> permission);
}
