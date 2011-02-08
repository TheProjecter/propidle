package acceptance.steps.givens;

import com.googlecode.propidle.authorisation.permissions.Permission;
import static com.googlecode.totallylazy.Sequences.sequence;
import static acceptance.steps.Callers.ensure;

import java.util.concurrent.Callable;
import java.util.List;
import java.util.ArrayList;

public class CurrentUserPermissions implements Callable<Void> {
    private final CurrentUser currentUser;
    private final UserPermissions userPermissions;

    private final List<Permission> permissionsToGrant = new ArrayList<Permission>();

    public CurrentUserPermissions(CurrentUser currentUser, UserPermissions userPermissions) {
        this.currentUser = currentUser;
        this.userPermissions = userPermissions;
    }

    public CurrentUserPermissions include(Permission... permissions) {
        permissionsToGrant.addAll(sequence(permissions).toList());
        return this;
    }

    public Void call() throws Exception {
        String username = "created by CurrentUserPermissions";
        ensure(userPermissions.forUser(username).include());
        ensure(currentUser.is(username));
        return null;
    }
}
