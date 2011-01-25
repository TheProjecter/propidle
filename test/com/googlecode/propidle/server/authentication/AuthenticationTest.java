package com.googlecode.propidle.server.authentication;

import static com.googlecode.propidle.authorisation.users.Password.password;
import static com.googlecode.propidle.authorisation.users.Username.username;
import com.googlecode.propidle.server.PropertiesApplicationTestCase;
import static com.googlecode.propidle.server.Values.with;
import static com.googlecode.propidle.server.steps.givens.UserDoesNotExist.userDoesNotExist;
import static com.googlecode.propidle.server.steps.givens.UserExists.userExists;
import static com.googlecode.propidle.server.steps.thens.Responses.*;
import static com.googlecode.propidle.server.steps.whens.RequestIsMade.weMakeRequest;
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
    public void shouldRedirectToDashboardOnSuccessfulLogin() throws Exception {
        given(userExists(with(username("bob")).and(password("bob's password"))));

        when(weMakeRequest(post("/authentication").
                withForm("username", "bob").
                withForm("password", "bob's password")));

        then(response(status()), is(Status.SEE_OTHER));
        then(response(header("location")), is("/"));
    }

    @Test
    public void shouldDisplayErrorMessageOnUnsuccessfulLogin() throws Exception {
        given(userDoesNotExist(with(username("captain kirk"))));

        when(weMakeRequest(post("/authentication").
                withForm("username", "captain kirk").
                withForm("password", "khaaaaaaaan!")));

        then(response(html()), matches(div("message", "Could not authenticate 'captain kirk' with the provided credentials")));
        then(response(html()), matches(input("username", "captain kirk")));
        then(response(html()), matches(input("password", "")));
    }
}
