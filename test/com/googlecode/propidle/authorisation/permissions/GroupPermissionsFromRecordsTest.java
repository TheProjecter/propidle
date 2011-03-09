package com.googlecode.propidle.authorisation.permissions;

import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.GroupId.newGroupId;
import static com.googlecode.propidle.authorisation.permissions.GroupPermission.groupPermissions;
import static com.googlecode.propidle.authorisation.permissions.Permission.permission;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.util.matchers.HasInAnyOrder.hasInAnyOrder;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class GroupPermissionsFromRecordsTest {
    private final GroupPermissionsFromRecords permissions = new GroupPermissionsFromRecords(testRecordsWithAllMigrationsRun());
    private static final GroupId GROUP_ID = newGroupId();
    private static final Sequence<Permission> SOME_PERMISSIONS = sequence(
            permission("eat football"),
            permission("sleep football"),
            permission("excrete lacrosse"));

    @Test
    public void canGrantPermissions() throws Exception {
        Sequence<GroupPermission> expected = sequence(groupPermissions(GROUP_ID, SOME_PERMISSIONS));

        Iterable<GroupPermission> result = permissions.grant(GROUP_ID, SOME_PERMISSIONS);

        assertThat(
                permissions.get(GROUP_ID),
                hasInAnyOrder(expected));
        assertThat(
                result,
                hasInAnyOrder(expected));
    }

    @Test
    public void willNotDuplicatePermissions() throws Exception {
        Sequence<GroupPermission> expected = sequence(groupPermissions(GROUP_ID, SOME_PERMISSIONS));

        permissions.grant(GROUP_ID, SOME_PERMISSIONS.join(SOME_PERMISSIONS));
        assertThat(
                permissions.get(GROUP_ID),
                hasInAnyOrder(expected));

        Iterable<GroupPermission> result = permissions.grant(GROUP_ID, SOME_PERMISSIONS);
        assertThat(
                result,
                hasInAnyOrder(expected));
        assertThat(
                permissions.get(GROUP_ID),
                hasInAnyOrder(expected));
    }

    @Test
    public void canRevokePermissions() throws Exception {
        Iterable<GroupPermission> expected = groupPermissions(GROUP_ID, SOME_PERMISSIONS.drop(2));

        permissions.grant(GROUP_ID, SOME_PERMISSIONS);
        Iterable<GroupPermission> result = permissions.revoke(GROUP_ID, SOME_PERMISSIONS.take(2));

        assertThat(
                result,
                hasInAnyOrder(sequence(expected)));
        assertThat(
                permissions.get(GROUP_ID),
                hasInAnyOrder(sequence(expected)));
    }
}
