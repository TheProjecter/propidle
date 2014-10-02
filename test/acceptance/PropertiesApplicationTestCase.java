package acceptance;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.WrapCallableInTransaction;
import com.googlecode.propidle.migrations.ModuleMigrationsCollector;
import com.googlecode.propidle.scheduling.StubScheduledExecutorService;
import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.MemoryResponse;
import com.googlecode.utterlyidle.Response;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.WithCustomRendering;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Matcher;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.util.TestRecords.hsqlConfiguration;
import static com.googlecode.propidle.util.TestRecords.runMigrations;
import static com.googlecode.yadic.Container.functions.get;
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

    private TestPropertiesApplication hsqlApplication() throws Exception {
        if (application == null) {
            final Properties configuration = hsqlConfiguration();
            application = new TestPropertiesApplication(configuration, new TestSupportModule(this, interestingGivens, capturedInputAndOutputs, executorService));
            runMigrations(application.applicationScope().add(ModuleMigrationsCollector.class), configuration);
        }
        return application;
    }


    protected void usingHsql() throws Exception {
        hsqlApplication();
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
        return inBusinessTransaction(get(something));
    }

    public Map<Class, Renderer> getCustomRenderers() {
        return Maps.map(Pair.<Class, Renderer>pair(MemoryResponse.class, new ResponseRenderer()));
    }

    protected String absoluteUrl(String s) {
        return application.basePath + s;
    }

    private class ResponseRenderer implements Renderer<Response> {
        public String render(Response response) throws Exception {
            return Xml.escape(format("%s\n\n%s", response.headers().toString(), response.entity().toString()));
        }
    }

}
