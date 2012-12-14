package com.googlecode.propidle.urls;

import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static com.googlecode.propidle.urls.StubHandler.stubHandler;
import static com.googlecode.propidle.server.ConvertRevisionNumberQueryParameterToHeader.REVISION_PARAM;
import static com.googlecode.propidle.server.RequestedRevisionNumber.requestedRevisionNumber;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.Status.BAD_REQUEST;
import static com.googlecode.utterlyidle.io.Converter.asString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class RelativeUriGetterTest {
    private static final BasePath BASE_PATH = basePath("/applicationRoot");
    private final UriGetter decorated = mock(UriGetter.class);

    @Test
    public void shouldResolveRelativeUrlsThroughTheApplication() throws Throwable {
        StubHandler lastRequest = stubHandler("WIN!");

        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(lastRequest), BASE_PATH, none(RequestedRevisionNumber.class));

        assertThat(asString(resolver.get(new URI("/someResource/path"), MimeType.TEXT_HTML)), is("WIN!"));
        assertThat(lastRequest.uri(), is(uri("/someResource/path")));

        verify(decorated, never()).get(any(URI.class), any(MimeType.class));
    }

    @Test
    public void shouldNotReplacePartOfPathWhichMatchesBasePath() throws Throwable {

        BasePath basePath = BasePath.basePath("/");

        StubHandler lastRequest = stubHandler("WIN!");

        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(lastRequest), basePath, none(RequestedRevisionNumber.class));

        assertThat(asString(resolver.get(new URI("some/resource"), MimeType.TEXT_HTML)), is("WIN!"));
        assertThat(lastRequest.uri(), is(uri("some/resource")));

        verify(decorated, never()).get(any(URI.class), any(MimeType.class));
    }

    @Test
    public void shouldStripBasePathFromFrontOfPath() throws Throwable {
        StubHandler lastRequest = stubHandler("WIN!");

        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(lastRequest), BASE_PATH, none(RequestedRevisionNumber.class));

        assertThat(asString(resolver.get(new URI(BASE_PATH.toString() + "someResource"), MimeType.TEXT_HTML)), is("WIN!"));
        assertThat(lastRequest.uri(), is(uri("someResource")));

        verify(decorated, never()).get(any(URI.class), any(MimeType.class));
    }

    @Test
    public void shouldThrowAnExceptionFor4xxAnd5xxStatusCodes() throws Throwable {
        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(stubHandler(BAD_REQUEST)), BASE_PATH, none(RequestedRevisionNumber.class));

        try {
            resolver.get(new URI(BASE_PATH.toString() + "someResource"), MimeType.TEXT_HTML);
            fail("Expected exception");
        } catch (HttpStatusCodeException e) {
            assertThat(e.statusCode(), is(BAD_REQUEST));
        }
    }

    @Test
    public void shouldPassDownRequestedRevisionNumber() throws Exception {
        StubHandler lastRequest = stubHandler("WIN!");

        Option<RequestedRevisionNumber> requestedRevision = some(requestedRevisionNumber("12"));
        RelativeUriGetter resolver = new RelativeUriGetter(decorated, usingRequestHandler(lastRequest), BASE_PATH, requestedRevision);

        resolver.get(new URI("/someResource"), MimeType.TEXT_HTML);
        assertThat(lastRequest.headers().getValue(REVISION_PARAM), is(requestedRevision.get().toString()));
    }

    private static Application usingRequestHandler(final HttpHandler httpHandler) {
        return new Application() {
            public Response handle(Request request) throws Exception {
                return httpHandler.handle(request);
            }

            public Container applicationScope() {
                throw new UnsupportedOperationException();
            }

            public <T> T usingRequestScope(Callable1<Container, T> callable1) {
                throw new UnsupportedOperationException();
            }

            public <T> T usingParameterScope(Request request, Callable1<Container, T> containerTCallable1) {
                throw new UnsupportedOperationException();
            }

            public <T> T usingArgumentScope(Request request, Callable1<Container, T> callable) {
                throw new UnsupportedOperationException();
            }

            public Container createRequestScope() {
                throw new UnsupportedOperationException();
            }

            public Application add(Module module) {
                throw new UnsupportedOperationException();
            }

            public void close() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void start() throws Exception {
                throw new UnsupportedOperationException();
            }

            @Override
            public void stop() throws Exception {
                throw new UnsupportedOperationException();
            }
        };
    }

}
