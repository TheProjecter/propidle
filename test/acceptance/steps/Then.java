package acceptance.steps;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Container;

import java.util.concurrent.Callable;

public class Then<T> implements Callable1<Container,T>{
    private final Callable1<Container, T> callable;

    public Then(Class<? extends Callable<T>> aClass) {
        this(new Step<T>(aClass));
    }

    public Then(Callable1<Container, T> callable) {
        this.callable = callable;
    }

    public <S> Then<S> map(final Callable1<T,S> map) {
        final Then<T> decorated = this;
        return new Then<S>(new Callable1<Container, S>() {
            public S call(Container container) throws Exception {
                return map.call(decorated.call(container));
            }
        });
    }

    public T call(Container container) throws Exception {
        return callable.call(container);
    }
}