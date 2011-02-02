package acceptance.steps.whens;

import acceptance.steps.WebClient;
import acceptance.steps.When;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;

import java.util.concurrent.Callable;

import static acceptance.Values.with;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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

    public static When<Response> browserRequests(final RequestBuilder request){
        return weMakeRequest(request.withHeader(HttpHeaders.ACCEPT, MediaType.TEXT_HTML));
    }

    public static When<Response> serviceReceivesA(final RequestBuilder request){
        return new When<Response>(RequestIsMade.class, with(request));
    }

    public Response call() throws Exception {
        webClient.handle(request);
        return webClient.currentPage();
    }
}
