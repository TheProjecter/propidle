package acceptance;

import acceptance.steps.givens.CurrentUser;
import acceptance.steps.givens.UserGroupExists;
import acceptance.steps.givens.UserPermissions;
import acceptance.steps.thens.LastResponse;
import static acceptance.steps.thens.LastResponse.*;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import com.googlecode.propidle.authorisation.groups.Group;
import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.Group.groupNameIs;
import static com.googlecode.propidle.authorisation.groups.Group.getGroupId;
import static com.googlecode.propidle.authorisation.permissions.Permission.MANAGE_USERS;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.totallylazy.Option;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

import static java.lang.String.format;

public class UserManagementTest extends PropertiesApplicationTestCase {
    @Test
    public void usersWithAppropriatePermissionsCanCreateOtherUsers() throws Exception {
        given(that(UserPermissions.class).forUser("big boss").include(MANAGE_USERS));
        given(that(CurrentUser.class).is("big boss"));

        given(that(UserGroupExists.class).with(groupName("losers")));

        when(a(RequestIsMade.class).to(post("/users/n00b").
                withForm("group", idOfGroup("losers")).
                withForm("password", "1337")));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theHeader("location"), inThe(LastResponse.class), is("/users/n00b"));

        when(a(RequestIsMade.class).to(get("/users/n00b")));

        then(theHtmlOf(), the(LastResponse.class), matches("losers"));
    }

    private String idOfGroup(String name) {
        Option<GroupId> groupId = sequence(capturedInputAndOutputs.values()).safeCast(Group.class).filter(groupNameIs(groupName(name))).map(getGroupId()).headOption();
        if(groupId.isEmpty()){
            throw new IllegalStateException(format("Group '%s' has not been created as part of the test", name));
        }
        return groupId.get().toString();
    }

}
