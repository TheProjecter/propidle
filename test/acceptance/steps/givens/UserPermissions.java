package acceptance.steps.givens;

import static acceptance.steps.Callers.ensure;
import com.googlecode.propidle.authorisation.groups.GroupName;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import com.googlecode.propidle.authorisation.permissions.Permission;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class UserPermissions implements Callable<Void> {
    private final AUserExists aUserExists;
    private final UserGroupExists userGroupExists;
    private final UserGroupPermissions userGroupPermissions;
    private final MembersOf membersOf;

    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();
    private Username username;

    public UserPermissions(AUserExists aUserExists, UserGroupExists userGroupExists, UserGroupPermissions userGroupPermissions, MembersOf membersOf) {
        this.aUserExists = aUserExists;
        this.userGroupExists = userGroupExists;
        this.userGroupPermissions = userGroupPermissions;
        this.membersOf = membersOf;
    }

    public Void call() throws Exception {
        GroupName groupName = groupName("created by UserPermissions for '" + username + "'");

        ensure(aUserExists.with(username));
        ensure(userGroupExists.with(groupName));
        ensure(userGroupPermissions.forGroup(groupName).contains(permissionsToGrant));
        ensure(membersOf.group(groupName).include(username));

        return null;
    }

    public UserPermissions forUser(String name) {
        this.username = username(name);
        return this;
    }

    public UserPermissions include(Permission... permissions) {
        this.permissionsToGrant.addAll(sequence(permissions).toList());
        return this;
    }
}
