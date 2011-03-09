package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.groups.*;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.concurrent.Callable;

import static acceptance.steps.Callers.ensure;

public class MembersOf implements Callable<Iterable<GroupMembership>> {
    private final GroupMemberships groupMemberships;
    private final UserGroupExists groupExists;
    private final AUserExists aUserExists;

    private GroupName groupName;
    private Username username;

    public MembersOf(GroupMemberships groupMemberships, UserGroupExists groupExists, AUserExists aUserExists) {
        this.groupMemberships = groupMemberships;
        this.groupExists = groupExists;
        this.aUserExists = aUserExists;
    }

    public Iterable<GroupMembership> call() throws Exception {
        Group group = ensure(groupExists.with(groupName));
        ensure(aUserExists.with(username));
        return groupMemberships.add(username, sequence(group.id()));
    }

    public MembersOf group(String groupName) {
        return group(GroupName.groupName(groupName));
    }

    public MembersOf group(GroupName groupName) {
        this.groupName = groupName;
        return this;
    }

    public MembersOf include(String username) {
        return include(Username.username(username));
    }

    public MembersOf include(Username username) {
        this.username = username;
        return this;
    }
}
