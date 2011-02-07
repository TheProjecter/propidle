package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.groups.GroupName;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import com.googlecode.propidle.authorisation.permissions.Permission;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authorisation.users.Username.username;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import acceptance.steps.Callers;
import static acceptance.steps.Callers.ensure;

public class UserPermissions implements Callable<Void> {
    private final UserExists userExists;
    private final UserGroupExists userGroupExists;
    private final UserGroupPermissions userGroupPermissions;
    private final MembersOf membersOf;

    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();
    private Username username;

    public UserPermissions(UserExists userExists, UserGroupExists userGroupExists, UserGroupPermissions userGroupPermissions, MembersOf membersOf) {
        this.userExists = userExists;
        this.userGroupExists = userGroupExists;
        this.userGroupPermissions = userGroupPermissions;
        this.membersOf = membersOf;
    }

    public Void call() throws Exception {
        GroupName groupName = groupName("auto-created to assign permissions to " + username);

        ensure(userExists.with(username));
        ensure(userGroupExists.with(groupName));
        ensure(userGroupPermissions.forGroup(groupName).contains(permissionsToGrant));
        ensure(membersOf.group(groupName).include(username));

        return null;
    }

    public UserPermissions forUser(String name) {
        this.username = username(name);
        return this;
    }

    public UserPermissions include(Permission permission) {
        this.permissionsToGrant.add(permission);
        return this;
    }
}
