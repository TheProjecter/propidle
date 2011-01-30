package acceptance;

import com.googlecode.propidle.TestPropertiesApplication;
import static com.googlecode.propidle.TestPropertiesApplication.inTransaction;
import acceptance.steps.*;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.utterlyidle.BasePath;
import static com.googlecode.utterlyidle.BasePath.basePath;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpecRunner.class)
public class PropertiesApplicationTestCase extends TestState {
    protected TestPropertiesApplication application = new TestPropertiesApplication();
    protected WebClient webClient = new WebClient(application);

    @Before
    public void addModules() {
        application.add(new WebClientModule());
    }

    protected <T> T given(Given<T> step) throws Exception {
        return perform(step);
    }

    protected <T> T when(When<T> step) {
        return perform(step);
    }

    protected <T> void then(Then<T> step, Matcher<? super T> matches) {
        assertThat(perform(step), matches);
    }

    private <T> T perform(Callable1<Container, T> step) {
        Container requestScope = application.createRequestScope();
        requestScope.remove(BasePath.class);
        requestScope.addInstance(BasePath.class, basePath("/"));

        Container container = new SimpleContainer(requestScope);
        container.addInstance(WebClient.class, webClient);
        container.addInstance(InterestingGivens.class, interestingGivens);
        container.addInstance(CapturedInputAndOutputs.class, capturedInputAndOutputs);
        return inTransaction(container, step);
    }
}
