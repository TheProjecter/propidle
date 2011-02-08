package acceptance;

import acceptance.steps.givens.CurrentUserPermissions;
import acceptance.steps.givens.MembersOf;
import acceptance.steps.givens.UserGroupExists;
import acceptance.steps.givens.AUserExists;
import acceptance.steps.thens.LastResponse;
import static acceptance.steps.thens.LastResponse.*;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.propidle.authorisation.groups.Group;
import static com.googlecode.propidle.authorisation.groups.Group.getGroupId;
import static com.googlecode.propidle.authorisation.groups.Group.groupNameIs;
import com.googlecode.propidle.authorisation.groups.GroupId;
import static com.googlecode.propidle.authorisation.groups.GroupName.groupName;
import static com.googlecode.propidle.authorisation.permissions.Permission.MANAGE_USERS;
import static com.googlecode.propidle.authorisation.users.Username.username;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.anchor;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.option;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import com.googlecode.propidle.util.matchers.RegexMatcher;
import com.googlecode.propidle.util.matchers.HtmlRegexes;
import com.googlecode.totallylazy.Option;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import static org.hamcrest.Matchers.is;
import org.hamcrest.Matcher;
import org.junit.Test;

import static java.lang.String.format;

public class UserManagementTest extends PropertiesApplicationTestCase {
    @Test
    public void usersWithAppropriatePermissionsCanCreateOtherUsers() throws Exception {
        given(that(CurrentUserPermissions.class).include(MANAGE_USERS));

        given(that(UserGroupExists.class).with(groupName("losers")));

        when(a(RequestIsMade.class).to(post("/users/n00b").
                withForm("addToGroup", idOfGroup("losers")).
                withForm("password", "1337")));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theHeader("location"), inThe(LastResponse.class), is("/users/n00b"));
    }

    @Test
    public void weCanSeeAListOfGroupsAUserBelongsTo() throws Exception {
        given(that(MembersOf.class).group("losers").include("n00b"));
        given(that(MembersOf.class).group("noodles").include("n00b"));

        when(a(RequestIsMade.class).to(get("/users/n00b")));

        then(theHtmlOf(), the(LastResponse.class), containsALinkToGroup("losers"));
        then(theHtmlOf(), the(LastResponse.class), containsALinkToGroup("noodles"));
    }

    @Test
    public void weCanAddANewGroupMembershipForAUser() throws Exception {
        given(that(CurrentUserPermissions.class).include(MANAGE_USERS));

        given(that(UserGroupExists.class).with(groupName("tasty things")));
        given(that(UserGroupExists.class).with(groupName("things that moo")));

        given(that(AUserExists.class).with(username("cow")));

        when(a(RequestIsMade.class).to(get("/users/cow")));

        then(theHtmlOf(), the(LastResponse.class), allowsSelectionOfGroup("tasty things"));
        then(theHtmlOf(), the(LastResponse.class), allowsSelectionOfGroup("things that moo"));
    }

    private Matcher<? super String> allowsSelectionOfGroup(String groupname) {
        return matches(option(idOfGroup(groupname), groupname));
    }

    private RegexMatcher containsALinkToGroup(String name) {
        return matches(anchor("/groups/" + name, name));
    }

    private String idOfGroup(String name) {
        Option<GroupId> groupId = sequence(capturedInputAndOutputs.values()).safeCast(Group.class).filter(groupNameIs(groupName(name))).map(getGroupId()).headOption();
        if (groupId.isEmpty()) {
            throw new IllegalStateException(format("Group '%s' has not been created as part of the test", name));
        }
        return groupId.get().toString();
    }

}
