package com.googlecode.propidle.urls;

import com.googlecode.propidle.server.RequestedRevisionNumber;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.googlecode.propidle.server.ConvertRevisionNumberQueryParameterToHeader.REVISION_PARAM;
import static com.googlecode.propidle.util.Exceptions.toException;
import static com.googlecode.totallylazy.Callers.callConcurrently;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.HttpHeaders.ACCEPT;

public class RelativeUriGetter implements UriGetter {
    private final UriGetter decorated;
    private final Application application;
    private final BasePath basePath;
    private final Option<RequestedRevisionNumber> requestedRevisionNumber;

    public RelativeUriGetter(UriGetter decorated, Application application, BasePath basePath, Option<RequestedRevisionNumber> requestedRevisionNumber) {
        this.decorated = decorated;
        this.application = application;
        this.basePath = basePath;
        this.requestedRevisionNumber = requestedRevisionNumber;
    }

    public InputStream get(URI uri, MimeType mimeType) throws Exception {
        if (uri.getScheme() == null && uri.getHost() == null) {
            try {
                // Make sure the other request happens on a different thread, in case we're using thread locals
                return callConcurrently(getRelativeUriTask(uri, mimeType)).apply();
            } catch (Throwable e) {
                Throwable cause = e.getCause();
                if(cause instanceof ExecutionException && cause.getCause() != null){
                    cause = cause.getCause();
                }
                throw toException(cause);
            }
        } else {
            return decorated.get(uri, mimeType);
        }
    }

    private Function<InputStream> getRelativeUriTask(final URI uri, final MimeType mimeType) {
        return new Function<InputStream>() {
            public InputStream call() throws Exception {
                return getRelativeUri(uri, mimeType);
            }
        };
    }

    private InputStream getRelativeUri(URI uri, MimeType mimeType) throws Exception {
        uri = stripBasePath(uri);
        RequestBuilder request = RequestBuilder.get(uri.toString()).replaceHeader(ACCEPT, mimeType.value());
        if(!requestedRevisionNumber.isEmpty()){
            request.header(REVISION_PARAM, requestedRevisionNumber.get().toString());
        }
        Response response = application.handle(request.build());

        validateResponseCode(response, uri);

        return new ByteArrayInputStream(response.entity().asBytes());
    }

    private void validateResponseCode(Response response, URI uri) {
        if (response.status().code() >= 400) {
            throw new HttpStatusCodeException(uri, response.status(), response.entity().toString());
        }
    }

    private URI stripBasePath(URI uri) {
        try {
            String newPath = stripBaseBath(uri.getRawPath());
            return new URI(uri.getScheme(), uri.getRawUserInfo(), uri.getHost(), uri.getPort(), newPath, uri.getRawQuery(), uri.getRawFragment());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not strip basepath from " + uri, e);
        }
    }

    private String stripBaseBath(String path) {
        if(path.startsWith(basePath.toString())) {
            return path.replaceFirst(basePath.toString(), "");
        }
        return path;
    }
}
