package acceptance.steps.thens;

import acceptance.steps.WebClient;
import static com.googlecode.propidle.properties.Properties.properties;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.ServerUrl;
import com.googlecode.utterlyidle.Status;

import static com.googlecode.utterlyidle.ServerUrl.serverUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Properties;
import java.util.concurrent.Callable;

public class LastResponse implements Callable<Response> {
    private final WebClient webClient;

    public LastResponse(WebClient webClient) {
        this.webClient = webClient;
    }

    public Response call() throws Exception {
        return webClient.currentPage();
    }

    public static Callable1<Response, Status> theStatusOf() {
        return new Callable1<Response, Status>() {
            public Status call(Response response) throws Exception {
                return response.status();
            }
        };
    }

    public static Callable1<Response, String> theHeader(final String headerName) {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                return response.header(headerName);
            }
        };
    }

    public static Callable1<Response, String> theLocationOf() {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                ServerUrl location = serverUrl(theHeader("location").call(response));
                return location.path().toString() + (location.getQuery() == null ? "" : "?" + location.getQuery());
            }
        };
    }

    public static Callable1<Response, String> theContentOf() {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                assertThat("Expected a successful response but got " + response.output(), response.status().code(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
                return response.output().toString();
            }
        };
    }

    public static Callable1<Response, String> theHtmlOf() {
        return theContentOf();
    }

    public static Callable1<Response, Properties> thePropertiesFileFrom() {
        return new Callable1<Response, Properties>() {
            public Properties call(Response response) throws Exception {
                return properties(response.output().toString());
            }
        };
    }
}
