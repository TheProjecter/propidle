import com.googlecode.utterlyidle.HttpHandler;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.authentication.api.Identity;
import com.googlecode.utterlyidle.authentication.application.PrivateResource;
import com.googlecode.utterlyidle.authentication.application.TestApplication;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import org.junit.Test;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.utterlyidle.FormParameters.formParameters;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.ResponseBuilder.response;
import static com.googlecode.utterlyidle.Status.OK;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IdentityTest {

    private final String validUser = "fred";

    @Test
    public void identityIsInjectedInTheContainer() throws Exception {
        TestApplication application = new TestApplication(new IdentityStubModule());

        application.handle(requestWith(validUser));

        assertThat(StubHttpHandler.identity.value(), is(validUser));
        application.shutdown();
    }

    private Request requestWith(String validUser) {
        return post("/" + PrivateResource.NAME + "utterlyidle.authenticate").forms(formParameters(asList(pair("username", validUser), pair("password", validUser)))).build();
    }

    public static class IdentityStubModule implements RequestScopedModule {
        @Override
        public Container addPerRequestObjects(Container container) throws Exception {
            return container.decorate(HttpHandler.class, StubHttpHandler.class);
        }
    }

    public static class StubHttpHandler implements HttpHandler{
        private final Container container;
        public static Identity identity;

        public StubHttpHandler(Container container) {
            this.container = container;
        }

        @Override
        public Response handle(Request request) throws Exception {
            identity = container.get(Identity.class);
            return response(OK).build();
        }
    }
}
