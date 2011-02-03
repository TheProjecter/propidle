package acceptance;

import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.Username.username;
import acceptance.steps.givens.UserExists;
import acceptance.steps.givens.UserDoesNotExist;
import static acceptance.steps.thens.LastResponse.*;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.util.HtmlRegexes.div;
import static com.googlecode.propidle.util.HtmlRegexes.input;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import com.googlecode.utterlyidle.Status;
import com.googlecode.yatspec.junit.SpecRunner;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpecRunner.class)
public class AuthenticationTest extends PropertiesApplicationTestCase {
    @Test
    public void onSuccessfulLoginClientIsRedirectedToRootUrl() throws Exception {
        given(that(UserExists.class).with(username("bob")).and(password("bob's password")));

        when(a(RequestIsMade.class).to(post("/authentication").
                withForm("username", "bob").
                withForm("password", "bob's password")));

        then(theStatusOf(), the(LastResponse.class), is(Status.SEE_OTHER));
        then(theHeader("location"), inThe(LastResponse.class), is("/"));
    }

    @Test
    public void onUnsuccessfulLoginAnErrorMessageIsDisplayed() throws Exception {
        given(that(UserDoesNotExist.class).with(username("captain kirk")));

        when(a(RequestIsMade.class).to(post("/authentication").
                withForm("username", "captain kirk").
                withForm("password", "khaaaaaaaan!")));

        then(theHtmlOf(), the(LastResponse.class), matches(div("message", "Could not authenticate 'captain kirk' with the provided credentials")));
        then(theHtmlOf(), the(LastResponse.class), matches(input("username", "captain kirk")));
        then(theHtmlOf(), the(LastResponse.class), matches(input("password", "")));
    }
}
