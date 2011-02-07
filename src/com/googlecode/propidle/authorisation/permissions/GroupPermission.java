package com.googlecode.propidle.authorisation.permissions;

import com.googlecode.propidle.authorisation.groups.GroupId;
import com.googlecode.totallylazy.Callable1;
import static com.googlecode.totallylazy.Sequences.sequence;

public class GroupPermission {
    private final GroupId groupId;
    private final Permission permission;

    public static Iterable<GroupPermission> groupPermissions(final GroupId groupId, Iterable<Permission> permissions) {
        return sequence(permissions).map(new Callable1<Permission, GroupPermission>() {
            public GroupPermission call(Permission permission) throws Exception {
                return groupPermission(groupId, permission);
            }
        });
    }

    public static GroupPermission groupPermission(GroupId groupId, Permission permission) {
        return new GroupPermission(groupId, permission);
    }

    private GroupPermission(GroupId groupId, Permission permission) {
        this.groupId = groupId;
        this.permission = permission;
    }

    public GroupId groupId() {
        return groupId;
    }

    public Permission permission() {
        return permission;
    }

    @Override
    public String toString() {
        return groupId + " can " + permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupPermission that = (GroupPermission) o;

        if (!groupId.equals(that.groupId)) return false;
        if (!permission.equals(that.permission)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + permission.hashCode();
        return result;
    }
}
