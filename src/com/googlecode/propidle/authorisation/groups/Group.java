package com.googlecode.propidle.authorisation.groups;

import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Callable1;

import static java.lang.String.format;

public class Group {
    private final GroupId id;
    private final GroupName name;

    public static Group group(GroupName name) {
        return group(GroupId.newGroupId(), name);
    }

    public static Group group(GroupId id, GroupName name) {
        return new Group(id, name);
    }

    protected Group(GroupId id, GroupName name) {
        if (name == null) throw new NullArgumentException("name");
        if (id == null) throw new NullArgumentException("id");
        this.name = name;
        this.id = id;
    }

    public GroupId id() {
        return id;
    }

    public GroupName name() {
        return name;
    }

    @Override
    public String toString() {
        return format("group '%s' (%s)", name, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!id.equals(group.id)) return false;
        if (!name.equals(group.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public static Predicate<? super Group> groupNameIs(final GroupName name) {
        return new Predicate<Group>() {
            public boolean matches(Group group) {
                return group.name().equals(name);
            }
        };
    }

    public static Callable1<? super Group, GroupId> getGroupId() {
        return new Callable1<Group, GroupId>() {
            public GroupId call(Group group) throws Exception {
                return group.id();
            }
        };
    }
}
