package acceptance;

import acceptance.steps.givens.AliasExists;
import acceptance.steps.thens.LastResponse;
import static acceptance.steps.thens.LastResponse.*;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.anchor;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.input;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static java.util.regex.Pattern.quote;

public class AliasesTest extends PropertiesApplicationTestCase {
    @Test
    public void ifAnAliasDoesNotExistThenTryingToGetItWillRedirectToTheEditPage() throws Exception {
        when(a(RequestIsMade.class).to(get("/aliases/production/myApplication/v123")));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theHeader("location"), the(LastResponse.class), is("/aliases/production/myApplication/v123?edit="));
    }

    @Test
    public void afterEditingAnAliasUsersAreRedirectedToTheEditPage() throws Exception {
        when(a(RequestIsMade.class).to(post("/aliases/production/myApplication/v123?edit=").
                withForm("to", "/some_other_url")));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theHeader("Location"), the(LastResponse.class), is("/aliases/production/myApplication/v123?edit="));
    }

    @Test
    public void aliasDetailsAreDisplayedOnTheEditPage() throws Exception {
        given(that(AliasExists.class).from(aliasPath("production/myApplication/v123")).to(aliasDestination("/some_other_url")));

        when(a(RequestIsMade.class).to(get("/aliases/production/myApplication/v123?edit")));

        then(theHtmlOf(), the(LastResponse.class), matches(anchor(quote("/some_other_url"))));
        then(theHtmlOf(), the(LastResponse.class), matches(input("to", quote("/some_other_url"))));
    }

    @Test
    public void onceCreatedAliasesWillRedirectToNewLocation() throws Exception {
        when(a(RequestIsMade.class).to(post("/aliases/production/myApplication/v123?edit=").
                withForm("to", "/some_other_url")));

        when(a(RequestIsMade.class).to(get("/aliases/production/myApplication/v123").
                withHeader(ACCEPT, TEXT_PLAIN)));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theHeader("location"), the(LastResponse.class), is("/some_other_url"));
    }

    @Test
    public void weCanSeeAListOfAllAvailableAliases() throws Exception {
        given(that(AliasExists.class).from(aliasPath("redirect_1")).to(aliasDestination("/properties/1")));
        given(that(AliasExists.class).from(aliasPath("redirect_2")).to(aliasDestination("/properties/2")));

        when(a(RequestIsMade.class).to(get("/aliases/")));

        then(theHtmlOf(), the(LastResponse.class), matches(anchor(quote("/aliases/redirect_1?edit="), "/aliases/redirect_1")));
        then(theHtmlOf(), the(LastResponse.class), matches(anchor("/properties/1")));
        then(theHtmlOf(), the(LastResponse.class), matches(anchor(quote("/aliases/redirect_2?edit="), "/aliases/redirect_2")));
        then(theHtmlOf(), the(LastResponse.class), matches(anchor("/properties/2")));
    }
}
