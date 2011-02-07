package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.groups.*;
import static com.googlecode.propidle.authorisation.groups.Group.getGroupId;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.concurrent.Callable;

public class MembersOf implements Callable<Iterable<GroupMembership>> {
    private final GroupMemberships groupMemberships;
    private final Groups groups;
    private GroupName groupName;
    private Username username;

    public MembersOf(GroupMemberships groupMemberships, Groups groups) {
        this.groupMemberships = groupMemberships;
        this.groups = groups;
    }

    public Iterable<GroupMembership> call() throws Exception {
        return groupMemberships.add(username, groups.get(groupName).map(getGroupId()));
    }

    public MembersOf group(GroupName groupName) {
        this.groupName = groupName;
        return this;
    }

    public MembersOf include(Username username) {
        this.username = username;
        return this;
    }
}
