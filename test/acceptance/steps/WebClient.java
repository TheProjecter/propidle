package acceptance.steps;

import com.googlecode.propidle.authentication.SessionId;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Request;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;

public class WebClient {
    private final Application application;
    private final TestLogger logger;
    private SessionId currentSession;
    private Response lastResponse;

    public WebClient(Application application, TestLogger logger) {
        this.application = application;
        this.logger = logger;
    }

    public Response handle(RequestBuilder requestBuilder) throws Exception {
        if(requestBuilder ==null) throw new NullArgumentException("request");
        if (currentSession != null) {
            requestBuilder.withHeader("cookies", "session=" + currentSession.value());
        }
        Request request = requestBuilder.build();
        Response response = application.handle(request);

        logger.log("request", request);
        logger.log("response", response);

        lastResponse = response;
        return response;
    }

    public Response currentPage(){
        return lastResponse;
    }

    public SessionId currentSession() {
        return currentSession;
    }

    public WebClient currentSession(SessionId sessionId) {
        this.currentSession = sessionId;
        return this;
    }
}
