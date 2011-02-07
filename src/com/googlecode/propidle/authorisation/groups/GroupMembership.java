package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.propidle.util.NullArgumentException;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.Callable1;

public class GroupMembership {
    private final Username username;
    private final GroupId groupId;

    public static Iterable<GroupMembership> groupMemberships(final Username username, Iterable<GroupId> groups) {
        return sequence(groups).map(new Callable1<GroupId, GroupMembership>() {
            public GroupMembership call(GroupId groupId) throws Exception {
                return groupMembership(username, groupId);
            }
        });
    }

    public static GroupMembership groupMembership(Username username, GroupId groupId) {
        return new GroupMembership(username, groupId);
    }

    private GroupMembership(Username username, GroupId groupId) {
        if (groupId == null) throw new NullArgumentException("groupId");
        if (username == null) throw new NullArgumentException("username");

        this.username = username;
        this.groupId = groupId;
    }

    public Username username() {
        return username;
    }

    public GroupId groupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return username + " is in group " + groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembership that = (GroupMembership) o;

        if (!groupId.equals(that.groupId)) return false;
        if (!username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + groupId.hashCode();
        return result;
    }

    public static Callable1<? super GroupMembership, GroupId> getGroupId() {
        return new Callable1<GroupMembership, GroupId>() {
            public GroupId call(GroupMembership groupMembership) throws Exception {
                return groupMembership.groupId();
            }
        };
    }
}
