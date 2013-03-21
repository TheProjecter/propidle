package com.googlecode.propidle.authentication;

import com.googlecode.propidle.security.SecurityModule;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.MediaType;
import java.io.IOException;

import static com.googlecode.utterlyidle.rendering.Model.model;
import static com.googlecode.propidle.security.SecurityModule.MODEL_NAME;

@Path(AuthenticationResource.NAME)
@Produces(MediaType.TEXT_HTML)
public class AuthenticationResource {
    public static final String UTTERLYIDLE_AUTHENTICATE = "utterlyidle.authenticate";
    public static final String ORIGINATING_REQUEST_FORM_NAME = "originatingRequest";
    private static final String BANNER_MESSAGE = "errorMessage";
    private static final String FAILED_LOGIN_COUNT = "login_count";

    public static final String NAME = "/authentication";
    private final AuthenticatedRequestRouter authenticatedRequestRouter;

    public AuthenticationResource(AuthenticatedRequestRouter authenticatedRequestRouter) {
        this.authenticatedRequestRouter = authenticatedRequestRouter;
    }

    @GET
    public Model authenticationPage(Request thisRequest, @QueryParam(ORIGINATING_REQUEST_FORM_NAME) final Option<String> originatingRequest, @QueryParam(FAILED_LOGIN_COUNT) Option<Integer> failedLoginCount) throws IOException {
        Request whereToGo = authenticatedRequestRouter.whereToGo(thisRequest, originatingRequest);
        final Model basicModel = model().
                add("originatingRequestTarget", redirectToAuthenticationHandler(whereToGo.uri())).
                add(SecurityModule.MODEL_NAME, NAME).
                add(ORIGINATING_REQUEST_FORM_NAME, whereToGo);
        return failedLoginCount.map(failedLoginFields(basicModel)).getOrElse(basicModel);
    }

    public static Uri redirectToAuthenticationHandler(Uri whereToGoUrl) {
        return whereToGoUrl.dropQuery().mergePath(UTTERLYIDLE_AUTHENTICATE);
    }

    private Callable1<Integer, Model> failedLoginFields(final Model basicModel) {
        return new Callable1<Integer, Model>() {
            public Model call(Integer integer) throws Exception {
                basicModel.add(BANNER_MESSAGE, "Login failed");
                return basicModel;
            }
        };
    }

}
