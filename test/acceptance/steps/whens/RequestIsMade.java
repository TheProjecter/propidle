package acceptance.steps.whens;

import acceptance.steps.WebClient;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;

import java.util.concurrent.Callable;

import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

public class RequestIsMade implements Callable<Response> {
    private final WebClient webClient;
    private RequestBuilder request;

    public RequestIsMade(WebClient webClient) {
        this.webClient = webClient;
    }

    public Response call() throws Exception {
        return webClient.handle(request.accepting(TEXT_HTML));
    }

    public RequestIsMade to(RequestBuilder request) {
        this.request = request;
        return this;
    }
}
