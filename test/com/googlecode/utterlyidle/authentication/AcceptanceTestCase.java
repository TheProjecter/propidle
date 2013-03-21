package com.googlecode.utterlyidle.authentication;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.authentication.application.TestApplication;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Matcher;
import org.junit.After;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class AcceptanceTestCase extends TestState {
    protected final TestApplication application = new TestApplication();

    @After
    public void clearSessionCookie() throws IOException {
        Response lastResponse = application.lastResponse();
        capturedInputAndOutputs.add("Last Response", lastResponse);
        capturedInputAndOutputs.add("Last Response Content", lastResponse.entity().toString());
        application.shutdown();
    }

    protected ActionUnderTest request(final Class<? extends Callable<Response>> requestType) {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                application.request(requestType);
                return capturedInputAndOutputs;
            }
        };
    }

    protected <T> T the(Class<T> something) throws Exception {
        return application.usingRequestScope(something);
    }

    public static Callable1<Response, Status> theStatusOf() {
        return new Callable1<Response, Status>() {
            public Status call(Response response) throws Exception {
                return response.status();
            }
        };
    }

    protected <T, V> void then(Callable1<T, V> map, Callable<T> step, Matcher<? super V> matches) throws Exception {
        assertThat(map.call(application.perform(step)), matches);
    }
}
