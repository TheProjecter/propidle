package acceptance.steps.thens;

import acceptance.steps.Then;
import acceptance.steps.WebClient;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Responses implements Callable<Response> {
    private final WebClient webClient;

    public Responses(WebClient webClient) {
        this.webClient = webClient;
    }

    public Response call() throws Exception {
        return webClient.currentPage();
    }

    public static <T> Then<T> response(Callable1<Response, T> map) {
        return new Then<Response>(Responses.class).map(map);
    }

    public static Callable1<Response, Status> status() {
        return method(on(Response.class).status());
    }

    public static Callable1<Response, String> header(String headerName) {
        return method(on(Response.class).header(headerName));
    }

    public static Callable1<Response, String> content() {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                assertThat("Expected a successful response but got " + response.output(), response.status().code(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
                return response.output().toString();
            }
        };
    }
    public static Callable1<Response, String> html() {
        return content();
    }
}
