package com.googlecode.propidle.authentication;

import com.googlecode.propidle.authentication.application.LastResponse;
import com.googlecode.propidle.authentication.application.PrivateResource;
import com.googlecode.propidle.authentication.requests.ActionUrl;
import com.googlecode.propidle.authentication.requests.GetPrivateResource;
import com.googlecode.propidle.authentication.requests.LoginByPrivilegedUser;
import com.googlecode.propidle.authentication.requests.LoginByUnprivilegedUser;
import com.googlecode.utterlyidle.BaseUri;
import com.googlecode.utterlyidle.BaseUriRedirector;
import com.googlecode.utterlyidle.RegisteredResources;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.googlecode.propidle.authentication.requests.ActionUrl.actionUrl;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.Status.FORBIDDEN;
import static com.googlecode.utterlyidle.Status.OK;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static org.hamcrest.Matchers.is;

@RunWith(SpecRunner.class)
public class AuthorisationTest extends AcceptanceTestCase {

    @Test
    public void forbidsAccessWhenUserLacksPermission() throws Exception {
        given(aResourceRequiringAuthorisation());
        and(anAuthenticatedUserWithNoPermissions());
        when(theUserRequestsAPrivateResource());
        then(theStatusOf(), the(LastResponse.class), is(FORBIDDEN));
    }

    @Test
    public void permitsAccessWhenUserHasPermission() throws Exception {
        given(aResourceRequiringAuthorisation());
        and(anAuthenticatedUserWithFullPermissions());
        when(theUserRequestsAPrivateResource());
        then(theStatusOf(), the(LastResponse.class), is(OK));
    }

    private GivensBuilder aResourceRequiringAuthorisation() {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                ActionUrl actionUrl = actionUrl(redirectorFor(PrivateResource.class).uriOf(method(on(PrivateResource.class).secretMessage())).toString());
                application.setActionUrl(actionUrl);
                interestingGivens.add("Action Url", actionUrl);
                return interestingGivens;
            }
        };
    }

    public static BaseUriRedirector redirectorFor(Class<?> resourceClass) {
        final RegisteredResources registeredResources = new RegisteredResources();
        registeredResources.add(annotatedClass(resourceClass));
        return new BaseUriRedirector(BaseUri.baseUri("/basePath"), registeredResources);
    }

    private GivensBuilder anAuthenticatedUserWithFullPermissions() {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                application.request(LoginByPrivilegedUser.class);
                return interestingGivens;
            }
        };
    }

    private ActionUnderTest theUserRequestsAPrivateResource() {
        return request(GetPrivateResource.class);
    }

    private GivensBuilder anAuthenticatedUserWithNoPermissions() {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                application.request(LoginByUnprivilegedUser.class);
                return interestingGivens;
            }
        };
    }

}
