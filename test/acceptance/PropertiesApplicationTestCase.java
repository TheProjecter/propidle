package acceptance;

import acceptance.steps.CloseTransaction;
import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.totallylazy.*;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Maps.map;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.MemoryResponse;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.rendering.WithCustomRendering;
import com.googlecode.yatspec.rendering.Renderer;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;
import java.util.Map;
import java.util.HashMap;
import static java.lang.String.format;

@RunWith(SpecRunner.class)
public abstract class PropertiesApplicationTestCase extends TestState implements WithCustomRendering {
    protected TestPropertiesApplication application;
    private Container businessTransaction;

    private TestPropertiesApplication application() throws Exception {
        if (application == null) {
            application = new TestPropertiesApplication(new TestSupportModule(this, interestingGivens, capturedInputAndOutputs));
        }
        return application;
    }

    protected <T> T given(Callable<T> step) throws Exception {
        return perform(step);
    }

    protected <T> T when(Callable<T> step) throws Exception {
        return perform(step);
    }

    protected <T> void then(Callable<T> step, Matcher<? super T> matches) throws Exception {
        then(Callables.<T>returnArgument(), step, matches);
    }

    protected <T, V> void then(Callable1<T, V> map, Callable<T> step, Matcher<? super V> matches) throws Exception {
        assertThat(map.call(perform(step)), matches);
    }

    @SuppressWarnings("unchecked")
    private <T> T perform(Callable<T> step) throws Exception {
        try {
            Container container = new SimpleContainer(businessTransaction());
            container.addInstance(Callable.class, step);
            container.add(CloseTransaction.class);
            return (T) container.get(CloseTransaction.class).call();
        } finally {
            businessTransaction = null;
        }
    }

    private Container businessTransaction() throws Exception {
        if (businessTransaction == null) {
            businessTransaction = new SimpleContainer(application().createRequestScope());
        }
        return businessTransaction;
    }

    protected <T> T that(Class<T> something) throws Exception {
        return create(something);
    }

    protected <T> T a(Class<T> something) throws Exception {
        return create(something);
    }

    protected <T> T the(Class<T> something) throws Exception {
        return create(something);
    }

    protected <T> T inThe(Class<T> something) throws Exception {
        return create(something);
    }

    private <T> T create(Class<T> something) throws Exception {
        return businessTransaction().get(something);
    }

    public Map<Class, Renderer> getCustomRenderers() {
        return map(Class.class, Renderer.class, pair(MemoryResponse.class, new ResponseRenderer()));
    }

    private class ResponseRenderer implements Renderer<Response>{
        public String render(Response response) throws Exception {
            return Strings.escapeXml(format("%s\n\n%s", response.toString(), Strings.toString(response.bytes())));
        }
    }
}
