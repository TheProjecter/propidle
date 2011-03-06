package com.googlecode.propidle.authentication;

import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.root.RootResource;
import com.googlecode.propidle.authentication.Session;
import com.googlecode.propidle.authentication.SessionStarter;
import com.googlecode.propidle.util.HumanReadable;
import com.googlecode.propidle.authorisation.users.Username;
import com.googlecode.propidle.authorisation.users.Password;
import com.googlecode.totallylazy.Either;

import static com.googlecode.utterlyidle.cookies.Cookie.cookie;
import static com.googlecode.utterlyidle.proxy.Resource.redirect;
import static com.googlecode.utterlyidle.proxy.Resource.resource;
import com.googlecode.utterlyidle.rendering.Model;
import static com.googlecode.utterlyidle.rendering.Model.model;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(AuthenticationResource.NAME)
public class AuthenticationResource {
    public static final String NAME = "authentication";
    private static final String SESSION_TOKEN = "session";

    private final SessionStarter sessionStarter;

    public AuthenticationResource(SessionStarter sessionStarter) {
        this.sessionStarter = sessionStarter;
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

            return redirect(resource(RootResource.class).get()).
                    cookie(SESSION_TOKEN, cookie(session.id().value()));
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
