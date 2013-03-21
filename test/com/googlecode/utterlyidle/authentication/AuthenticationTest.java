package com.googlecode.utterlyidle.authentication;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.authentication.application.LastResponse;
import com.googlecode.utterlyidle.authentication.application.TestResource;
import com.googlecode.utterlyidle.authentication.requests.*;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.authentication.AuthenticationResource.ORIGINATING_REQUEST_FORM_NAME;
import static com.googlecode.utterlyidle.authentication.AuthorisationTest.redirectorFor;
import static com.googlecode.utterlyidle.authentication.requests.ActionUrl.actionUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpecRunner.class)
public class AuthenticationTest extends AcceptanceTestCase {
    private static final String ROOT_RESOURCE = "root resource";

    @Test
    public void restrictedResourcesRequireAuthentication() throws Exception {
        given(aResourceRequiringAuthenticationForPosts());
        when(theUserMakesAnUnauthenticatedPostRequest());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString(ORIGINATING_REQUEST_FORM_NAME));
        then(theContentOf(), the(LastResponse.class), containsString("username"));
        then(theContentOf(), the(LastResponse.class), containsString("password"));

        when(theUserAuthenticatesSuccessfully());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You posted something"));

        when(theUserMakesAnUnauthenticatedPostRequest());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You posted something"));
    }

    private GivensBuilder aResourceRequiringAuthenticationForPosts() {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                ActionUrl actionUrl = actionUrl(redirectorFor(TestResource.class).uriOf(method(on((TestResource.class)).postHello())).toString());
                application.setActionUrl(actionUrl);
                interestingGivens.add("Action Url", actionUrl);
                return interestingGivens;
            }
        };
    }

    @Test
    public void failureToAuthenticateSuccessfullyAllowsRetry() throws Exception {
        given(aResourceRequiringAuthenticationForPosts());
        when(theUserMakesAnUnauthenticatedPostRequest());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("username"));
        then(theContentOf(), the(LastResponse.class), containsString("password"));

        when(theUserEntersInvalidCredentials());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("Login failed"));

        when(theUserAuthenticatesSuccessfully());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You posted something"));
    }

    @Test
    public void authenticatingDirectlyGoesToTheApplicationHomePage() throws Exception {
        given(aResourceRequiringAuthenticationForPosts());
        when(theUserAuthenticatesSuccessfully());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString(ROOT_RESOURCE));
    }

    @Test
    public void unrestrictedAccessDoesNotRequireAuthenticationButDoesGetSessionCookie() throws Exception {
        when(anUnauthenticatedUserMakesAGetRequest());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You got something"));
    }

    @Test
    public void aUserNavigatesDirectlyToTheAuthenticationResource() throws Exception {
        given(aResourceRequiringAuthenticationForPosts());

        when(anUnauthenticatedUserGetsTheAuthenticationPage());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("Login"));

        when(theUserAuthenticatesSuccessfully());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString(ROOT_RESOURCE));
    }

    @Test
    public void userIsAuthenticatedWhileSessionIsAlive() throws Exception {
        given(aResourceRequiringAuthenticationForPosts());

        when(theUserMakesAnUnauthenticatedPostRequest());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("username"));
        then(theContentOf(), the(LastResponse.class), containsString("password"));

        when(theUserAuthenticatesSuccessfully());

        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You posted something"));

        when(theUserMakesAnUnauthenticatedPostRequest());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("You posted something"));

        when(theSessionExpires());
        when(theUserMakesAnUnauthenticatedPostRequest());
        then(theStatusOf(), the(LastResponse.class), is(Status.OK));
        then(theContentOf(), the(LastResponse.class), containsString("username"));
        then(theContentOf(), the(LastResponse.class), containsString("password"));
        then(theContentOf(), the(LastResponse.class), containsString(ORIGINATING_REQUEST_FORM_NAME));

    }

    private ActionUnderTest theSessionExpires() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                application.clearSessions();
                return capturedInputAndOutputs;
            }
        };
    }

    //These methods don't care if the user is authenticated or not - Tidy up

    private ActionUnderTest anUnauthenticatedUserGetsTheAuthenticationPage() {
        return request(com.googlecode.utterlyidle.authentication.requests.GetAuthenticationResourceRequest.class);
    }

    private ActionUnderTest theUserEntersInvalidCredentials() {
        return request(FailedLoginRequest.class);
    }

    private ActionUnderTest theUserAuthenticatesSuccessfully() {
        return request(LoginByUnprivilegedUser.class);
    }

    private ActionUnderTest theUserMakesAnUnauthenticatedPostRequest() {
        return request(PostRequest.class);
    }

    private ActionUnderTest anUnauthenticatedUserMakesAGetRequest() {
        return request(com.googlecode.utterlyidle.authentication.requests.GetRequest.class);
    }


    public static Callable1<Response, String> theHeader(final String headerName) {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                return response.headers().getValue(headerName);
            }
        };
    }

    public static Callable1<Response, String> theContentOf() {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                final String content = response.entity().toString();
                assertThat("Expected a successful response but got " + content, response.status().code(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
                return content;
            }
        };
    }

    public static Callable1<Response, Iterable<String>> theHeaderValue(final String name) {
        return new Callable1<Response, Iterable<String>>() {
            public Iterable<String> call(Response response) throws Exception {
                assertThat("Expected a successful response but got " + response.entity(), response.status().code(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
                return response.headers().getValues(name);
            }
        };
    }
}
