package com.googlecode.propidle.server;

import static com.googlecode.propidle.server.ConvertRevisionNumberQueryParameterToHeader.REVISION_PARAM;
import static com.googlecode.propidle.server.RequestedRevisionNumber.requestedRevisionNumber;
import com.googlecode.utterlyidle.Request;
import com.googlecode.yadic.ContainerException;

import java.util.concurrent.Callable;
import static java.lang.String.format;

public class RequestedRevisionNumberActivator implements Callable<RequestedRevisionNumber> {
    private final Request request;

    public RequestedRevisionNumberActivator(Request request) {
        this.request = request;
    }

    public RequestedRevisionNumber call() throws Exception {
        String header = request.headers().getValue(REVISION_PARAM);
        if (header == null) {
            throw new ContainerException(format("%s parameter not found in request headers", REVISION_PARAM));
        }
        return requestedRevisionNumber(header);
    }
}
