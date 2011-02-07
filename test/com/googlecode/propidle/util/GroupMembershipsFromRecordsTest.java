package com.googlecode.propidle.util;

import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.GroupId.newGroupId;
import com.googlecode.propidle.authorisation.groups.GroupMembership;
import static com.googlecode.propidle.authorisation.groups.GroupMembership.groupMemberships;
import com.googlecode.propidle.authorisation.groups.GroupMembershipsFromRecords;
import static com.googlecode.propidle.authorisation.groups.GroupMembershipsFromRecords.defineGroupMembershipsRecord;
import com.googlecode.propidle.authorisation.users.Username;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.util.HasInAnyOrder.hasInAnyOrder;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class GroupMembershipsFromRecordsTest {
    private final GroupMembershipsFromRecords memberships = new GroupMembershipsFromRecords(defineGroupMembershipsRecord(temporaryRecords()));
    private static final Username USERNAME = username("withnail");
    private static final Sequence<GroupId> SOME_GROUPS = sequence(
            newGroupId(),
            newGroupId(),
            newGroupId());

    @Test
    public void canGrantPermissions() throws Exception {
        Sequence<GroupMembership> expected = sequence(groupMemberships(USERNAME, SOME_GROUPS));

        Iterable<GroupMembership> result = memberships.add(USERNAME, SOME_GROUPS);

        assertThat(
                result,
                hasInAnyOrder(expected));
        assertThat(
                memberships.get(USERNAME),
                hasInAnyOrder(expected));
    }

    @Test
    public void willNotDuplicatePermissions() throws Exception {
        Sequence<GroupMembership> expected = sequence(groupMemberships(USERNAME, SOME_GROUPS));

        memberships.add(USERNAME, SOME_GROUPS.join(SOME_GROUPS));
        assertThat(
                memberships.get(USERNAME),
                hasInAnyOrder(expected));

        Iterable<GroupMembership> result = memberships.add(USERNAME, SOME_GROUPS);
        assertThat(
                result,
                hasInAnyOrder(expected));
        assertThat(
                memberships.get(USERNAME),
                hasInAnyOrder(expected));
    }

    @Test
    public void canRevokePermissions() throws Exception {
        Iterable<GroupMembership> expected = groupMemberships(USERNAME, SOME_GROUPS.drop(2));

        memberships.add(USERNAME, SOME_GROUPS);
        Iterable<GroupMembership> result = memberships.remove(USERNAME, SOME_GROUPS.take(2));

        assertThat(
                result,
                hasInAnyOrder(expected));
        assertThat(
                memberships.get(USERNAME),
                hasInAnyOrder(expected));
    }
}
