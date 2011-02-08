package com.googlecode.propidle.authorisation.groups;

import static com.googlecode.propidle.authorisation.groups.Group.group;
import static com.googlecode.propidle.authorisation.groups.GroupId.newGroupId;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import static com.googlecode.propidle.authorisation.groups.GroupsFromRecords.defineGroupsRecord;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.propidle.util.matchers.HasInAnyOrder.hasInAnyOrder;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

public class GroupsFromRecordsTest {
    private final Groups groups = new GroupsFromRecords(defineGroupsRecord(temporaryRecords()));

    @Test
    public void canAddAndRetrieveGroups() throws Exception {
        Group group = group(groupName("test group"));
        groups.add(group);

        assertThat(groups.get(sequence(group.id())), hasExactly(group));
        assertThat(groups.get(group.name()), is((Option) some(group)));

        assertThat(groups.get(sequence(newGroupId())), is(Matchers.<Group>emptyIterable()));
    }

    @Test
    public void canGetAllGroups() throws Exception {
        Group group1 = group(groupName("group 1"));
        Group group2 = group(groupName("group 2"));

        groups.add(group1);
        groups.add(group2);

        assertThat(groups.get(), hasInAnyOrder(group1, group2));
    }
}
