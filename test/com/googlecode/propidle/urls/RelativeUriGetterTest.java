package com.googlecode.propidle.urls;

import com.googlecode.propidle.requesthandlers.RememberRequest;
import com.googlecode.propidle.requesthandlers.SetResponse;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import org.junit.Test;

import java.net.URI;

import static com.googlecode.propidle.requesthandlers.RememberRequest.rememberRequest;
import static com.googlecode.propidle.requesthandlers.RequestHandlerChain.chain;
import static com.googlecode.propidle.requesthandlers.SetResponse.setResponse;
import static com.googlecode.propidle.requesthandlers.SetStatusCode.setStatusCode;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.io.Converter.asString;
import static com.googlecode.utterlyidle.io.Url.url;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class RelativeUriGetterTest {
    private static final BasePath BASE_PATH = basePath("/applicationRoot");
    private final UriGetter decorated = mock(UriGetter.class);

    @Test
    public void shouldResolveRelativeUrlsThroughTheApplication() throws Throwable {
        RememberRequest lastRequest = rememberRequest();
        SetResponse setResponse = setResponse("WIN!");

        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(chain(lastRequest, setResponse)), BASE_PATH);

        assertThat(asString(resolver.get(new URI("/someResource"), MimeType.TEXT_HTML)), is("WIN!"));
        assertThat(lastRequest.url(), is(url("/someResource")));

        verify(decorated, never()).get(any(URI.class), any(MimeType.class));
    }

    @Test
    public void shouldStripBasePathFromFrontOfPath() throws Throwable {
        RememberRequest lastRequest = new RememberRequest();
        SetResponse setResponse = setResponse("WIN!");

        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(chain(lastRequest, setResponse)), BASE_PATH);

        assertThat(asString(resolver.get(new URI(BASE_PATH.toString() + "someResource"), MimeType.TEXT_HTML)), is("WIN!"));
        assertThat(lastRequest.url(), is(url("someResource")));

        verify(decorated, never()).get(any(URI.class), any(MimeType.class));
    }

    @Test
    public void shouldThrowAnExceptionFor4xxAnd5xxStatusCodes() throws Throwable {
        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(setStatusCode(Status.BAD_REQUEST)), BASE_PATH);

        try {
            resolver.get(new URI(BASE_PATH.toString() + "someResource"), MimeType.TEXT_HTML);
            fail("Expected exception");
        } catch (HttpStatusCodeException e) {
            assertThat(e.statusCode(), is(Status.BAD_REQUEST));
        }
    }

    private static Application usingRequestHandler(final HttpHandler httpHandler) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                httpHandler.handle(request, response);
            }

            public Container applicationScope() {
                throw new UnsupportedOperationException();
            }

            public Container createRequestScope() {
                throw new UnsupportedOperationException();
            }

            public Application add(Module module) {
                throw new UnsupportedOperationException();
            }

            public Resources resources() {
                throw new UnsupportedOperationException();
            }

            public ResponseHandlers responseHandlers() {
                throw new UnsupportedOperationException();
            }
        };
    }

}
