package acceptance.steps.givens;

import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import com.googlecode.propidle.authorisation.groups.GroupName;
import com.googlecode.propidle.authorisation.groups.Groups;
import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.Group.getGroupId;
import com.googlecode.propidle.authorisation.permissions.Permission;
import com.googlecode.propidle.authorisation.permissions.GroupPermissions;
import com.googlecode.propidle.authorisation.permissions.GroupPermission;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.Option;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.ArrayList;
import static java.lang.String.format;

public class UserGroupPermissions implements Callable<Iterable<GroupPermission>>{
    private final Groups groups;
    private final GroupPermissions permissions;

    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();
    private GroupName groupName;

    public UserGroupPermissions(Groups groups, GroupPermissions permissions) {
        this.groups = groups;
        this.permissions = permissions;
    }

    public UserGroupPermissions forGroup(String name) {
        return forGroup(groupName(name));
    }

    public UserGroupPermissions forGroup(GroupName groupName) {
        this.groupName = groupName;
        return this;
    }

    public UserGroupPermissions contains(Permission permission) {
        return contains(sequence(permission));
    }

    public UserGroupPermissions contains(Iterable<Permission> permissions) {
        this.permissionsToGrant.addAll(sequence(permissions).toList());
        return this;
    }

    public Iterable<GroupPermission> call() throws Exception {
        Option<GroupId> groupId = groups.get(groupName).map(getGroupId());
        if(groupId.isEmpty()){
            throw new IllegalStateException(format("Group '%s' does not exist", groupName));
        }
        return permissions.grant(groupId.get(), permissionsToGrant);
    }
}
