package com.googlecode.propidle.authentication;

import com.googlecode.propidle.authentication.SessionCookieGrantingHttpHandler;
import com.googlecode.utterlyidle.*;
import org.junit.Test;

import static com.googlecode.utterlyidle.HttpHeaders.COOKIE;
import static com.googlecode.utterlyidle.HttpHeaders.SET_COOKIE;
import static com.googlecode.utterlyidle.Responses.response;
import static com.googlecode.utterlyidle.cookies.CookieParameters.cookies;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SessionCookieGrantingHttpHandlerTest {

    private StubHandler decoratedHandler = new StubHandler();
    private Request anyRequest = RequestBuilder.get("whatever").build();

    @Test
    public void assignSessionIdToRequestAndResponse() throws Exception {
        SessionCookieGrantingHttpHandler handler = new SessionCookieGrantingHttpHandler(decoratedHandler);
        decoratedHandler.respond(response());

        Response response = handler.handle(anyRequest);

        assertThat(decoratedHandler.request().headers().getValue(COOKIE), is(not(nullValue())));
        assertThat(decoratedHandler.request().headers().getValue(COOKIE), is(equalTo(response.headers().getValue(SET_COOKIE))));
    }

    private String cookiesFrom(final HeaderParameters headerParameters) {
        return cookies(headerParameters).getValue(SessionCookieGrantingHttpHandler.SESSION_COOKIE_NAME);
    }

    private class StubHandler implements HttpHandler {
        private Request request;
        private Response response;

        public Response handle(Request request) throws Exception {
            this.request = request;
            return response;
        }

        public Request request() {
            return request;
        }

        public void respond(Response response) {
            this.response = response;
        }
    }
}
