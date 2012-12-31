package acceptance.steps;

import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;

public class WebClient {
    private final Application application;
    private final TestLogger logger;
    private Response lastResponse;

    public WebClient(Application application, TestLogger logger) {
        this.application = application;
        this.logger = logger;
    }

    public Response handle(RequestBuilder requestBuilder) throws Exception {
        if(requestBuilder ==null) throw new NullArgumentException("request");
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
}
