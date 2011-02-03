package acceptance;

import acceptance.steps.WebClient;
import acceptance.steps.WebClientModule;
import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.persistence.Transaction;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.utterlyidle.BasePath;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.apache.lucene.index.IndexWriter;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.concurrent.Callable;

@RunWith(SpecRunner.class)
public abstract class PropertiesApplicationTestCase extends TestState {
    protected TestPropertiesApplication application;
    protected WebClient webClient;
    private Container businessTransaction;

    @Before
    public void addModules() throws Exception {
        application().add(new WebClientModule());
    }

    @Before
    public void createWebClient() throws Exception {
        webClient = new WebClient(application());
    }

    private TestPropertiesApplication application() throws Exception {
        if (application == null) {
            application = new TestPropertiesApplication();
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
        try{
            Container container = new SimpleContainer(businessTransaction());
            container.addInstance(Callable.class, step);
            container.add(CloseTransactionAfter.class);
            return (T) container.get(CloseTransactionAfter.class).call();
        }finally{
            businessTransaction = null;
        }
    }

    private Container businessTransaction() throws Exception {
        if (businessTransaction == null) {
            Container requestScope = application().createRequestScope();
            requestScope.remove(BasePath.class);
            requestScope.addInstance(BasePath.class, basePath("/"));

            businessTransaction = new SimpleContainer(requestScope);
            businessTransaction.addInstance(WebClient.class, webClient);
            businessTransaction.addInstance(InterestingGivens.class, interestingGivens);
            businessTransaction.addInstance(CapturedInputAndOutputs.class, capturedInputAndOutputs);
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
        Container container = businessTransaction();
        container.add(something);
        return container.get(something);
    }

    public static class CloseTransactionAfter implements Callable {
        private final IndexWriter indexWriter;
        private final Transaction transaction;
        private final Callable step;

        public CloseTransactionAfter(IndexWriter indexWriter, Transaction transaction, Callable step) {
            this.indexWriter = indexWriter;
            this.transaction = transaction;
            this.step = step;
        }

        public Object call() throws Exception {
            Object result;
            try {
                result = step.call();
                indexWriter.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Did not commit transaction", e);
            }
            transaction.commit();
            return result;

        }
    }
}
