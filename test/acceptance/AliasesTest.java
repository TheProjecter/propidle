package acceptance;

import acceptance.PropertiesApplicationTestCase;
import static acceptance.Values.with;
import static acceptance.steps.whens.RequestIsMade.weMakeRequest;
import static acceptance.steps.thens.Responses.*;
import static acceptance.steps.givens.AliasExists.aliasExists;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.propidle.util.HtmlRegexes.a;
import static com.googlecode.propidle.util.HtmlRegexes.input;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static java.util.regex.Pattern.quote;

public class AliasesTest extends PropertiesApplicationTestCase {
    @Test
    public void ifAnAliasDoesNotExistThenTryingToGetItWillRedirectToTheEditPage() throws Exception {
        when(weMakeRequest(get("/aliases/production/myApplication/v123")));

        then(response(status()), is(SEE_OTHER));
        then(response(header("location")), is("/aliases/production/myApplication/v123?edit="));
    }

    @Test
    public void afterEditingAnAliasUsersAreRedirectedToTheEditPage() throws Exception {
        when(weMakeRequest(post("/aliases/production/myApplication/v123?edit=").
                withForm("to", "/some_other_url")));

        then(response(status()), is(SEE_OTHER));
        then(response(header("Location")), is("/aliases/production/myApplication/v123?edit="));
    }

    @Test
    public void aliasDetailsAreDisplayedOnTheEditPage() throws Exception {
        given(aliasExists(with(aliasPath("production/myApplication/v123")).and(aliasDestination("/some_other_url"))));

        when(weMakeRequest(get("/aliases/production/myApplication/v123?edit")));

        then(response(html()), matches(a(quote("/some_other_url"))));
        then(response(html()), matches(input("to", quote("/some_other_url"))));
    }

    @Test
    public void onceCreatedAliasesWillRedirectToNewLocation() throws Exception {
        when(weMakeRequest(post("/aliases/production/myApplication/v123?edit=").
                withForm("to", "/some_other_url")));

        when(weMakeRequest(get("/aliases/production/myApplication/v123").
                withHeader(ACCEPT, TEXT_PLAIN)));

        then(response(status()), is(SEE_OTHER));
        then(response(header("location")), is("/some_other_url"));
    }

    @Test
    public void weCanSeeAListOfAllAvailableAliases() throws Exception {
        given(aliasExists(with(aliasPath("redirect_1")).and(aliasDestination("/properties/1"))));
        given(aliasExists(with(aliasPath("redirect_2")).and(aliasDestination("/properties/2"))));

        when(weMakeRequest(get("/aliases/")));

        then(response(html()), matches(a(quote("/aliases/redirect_1?edit="), "/aliases/redirect_1")));
        then(response(html()), matches(a("/properties/1")));
        then(response(html()), matches(a(quote("/aliases/redirect_2?edit="), "/aliases/redirect_2")));
        then(response(html()), matches(a("/properties/2")));
    }
}
