package com.googlecode.propidle.server.authentication;

import com.googlecode.propidle.authentication.*;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.server.dashboard.DashboardResource;
import com.googlecode.propidle.server.sessions.Session;
import com.googlecode.propidle.server.sessions.SessionStarter;
import com.googlecode.propidle.util.HumanReadable;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.propidle.authorisation.users.Password;
import com.googlecode.totallylazy.Either;
import com.googlecode.utterlyidle.cookies.CookieName;
import static com.googlecode.utterlyidle.cookies.CookieName.cookieName;
import com.googlecode.utterlyidle.cookies.Cookies;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import com.googlecode.utterlyidle.rendering.Model;
import static com.googlecode.utterlyidle.rendering.Model.model;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(AuthenticationResource.NAME)
public class AuthenticationResource {
    public static final String NAME = "authentication";
    private static final CookieName SESSION_TOKEN = cookieName("session");

    private final SessionStarter sessionStarter;
    private final Cookies cookies;

    public AuthenticationResource(SessionStarter sessionStarter, Cookies cookies) {
        this.sessionStarter = sessionStarter;
        this.cookies = cookies;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Model get() {
        return modelOf(null, null);
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public Object post(@FormParam("username") Username username, @FormParam("password") Password password) {
        Either<Session, AuthenticationException> authenticationResult =
                sessionStarter.start(username, password);

        if (authenticationResult.isRight()) {
            return modelOf(authenticationResult.right());
        } else {
            Session session = authenticationResult.left();

            cookies.set(SESSION_TOKEN, session.id().value());
            cookies.commit();

            return redirect(resource(DashboardResource.class).get());
        }
    }

    private Model modelOf(AuthenticationException exception) {
        return modelOf(exception.username(), exception.userMessage());
    }

    private Model modelOf(Username username, HumanReadable message) {
        return model().
                add(PropertiesModule.MODEL_NAME, NAME).
                add("username", username).
                add("message", message);
    }
}
