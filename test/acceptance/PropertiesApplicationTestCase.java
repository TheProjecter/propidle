package acceptance;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.propidle.scheduling.RunnableRequest;
import com.googlecode.propidle.scheduling.StubScheduledExecutorService;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.Xml;
import com.googlecode.utterlyidle.MemoryResponse;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.WithCustomRendering;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Matcher;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Pair.pair;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpecRunner.class)
public abstract class PropertiesApplicationTestCase extends TestState implements WithCustomRendering {
    protected TestPropertiesApplication application;
    protected final StubScheduledExecutorService executorService = new StubScheduledExecutorService();

    private TestPropertiesApplication application() throws Exception {
        if (application == null) {
            application = new TestPropertiesApplication(new TestSupportModule(this, interestingGivens, capturedInputAndOutputs, executorService));
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
    private <T> T perform(final Callable<T> step) throws Exception {
        return inBusinessTransaction(new Callable1<Container, T>() {
            public T call(Container requestScope) throws Exception {
                Container container = new SimpleContainer(requestScope);
                container.addInstance(Callable.class, step);
                container.decorate(Callable.class, WrapCallableInTransaction.class);
                return (T) container.get(Callable.class).call();
            }
        });
    }

    protected <T> T inBusinessTransaction(Callable1<Container, T> callable1) throws Exception {
        return application().usingRequestScope(callable1);
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

    private <T> T create(final Class<T> something) throws Exception {
        return inBusinessTransaction(new Callable1<Container, T>() {
            public T call(Container container) throws Exception {
                return container.get(something);
            }
        });
    }

    public Map<Class, Renderer> getCustomRenderers() {
        return Maps.map(Pair.<Class, Renderer>pair(MemoryResponse.class, new ResponseRenderer()));
    }

    private class ResponseRenderer implements Renderer<Response> {
        public String render(Response response) throws Exception {
            return Xml.escape(format("%s\n\n%s", response.toString(), Strings.toString(response.bytes())));
        }
    }

}
