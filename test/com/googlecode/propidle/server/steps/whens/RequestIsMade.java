package com.googlecode.propidle.server.steps.whens;

import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.propidle.server.steps.WebClient;
import com.googlecode.propidle.server.steps.When;
import static com.googlecode.propidle.server.Values.with;

import java.util.concurrent.Callable;

public class RequestIsMade implements Callable<Response> {
    private final RequestBuilder request;
    private final WebClient webClient;

    public RequestIsMade(RequestBuilder request, WebClient webClient) {
        this.request = request;
        this.webClient = webClient;
    }

    public static When<Response> weMakeRequest(final RequestBuilder request){
        return serviceReceivesA(request);
    }

    public static When<Response> serviceReceivesA(final RequestBuilder request){
        return new When<Response>(RequestIsMade.class, with(request));
    }

    public Response call() throws Exception {
        webClient.handle(request);
        return webClient.currentPage();
    }
}
