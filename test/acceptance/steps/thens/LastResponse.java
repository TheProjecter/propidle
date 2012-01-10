package acceptance.steps.thens;

import acceptance.steps.WebClient;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.totallylazy.Uri.uri;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
                Uri location = uri(theHeader("location").call(response));
                return location.dropScheme().dropAuthority().toString();
            }
        };
    }

    public static Callable1<Response, String> theContentOf() {
        return new Callable1<Response, String>() {
            public String call(Response response) throws Exception {
                final String html = new String(response.bytes());
                assertThat("Expected a successful response but got " + html, response.status().code(), allOf(greaterThanOrEqualTo(200), lessThan(300)));
                return html;
            }
        };
    }

    public static Callable1<Response, String> theHtmlOf() {
        return theContentOf();
    }

    public static Callable1<Response, Properties> thePropertiesFileFrom() {
        return new Callable1<Response, Properties>() {
            public Properties call(Response response) throws Exception {
                return properties(new String(response.bytes()));
            }
        };
    }
}