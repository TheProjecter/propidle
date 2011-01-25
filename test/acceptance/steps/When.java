package acceptance.steps;

import acceptance.Values;

import java.util.concurrent.Callable;

public class When<T> extends Step<T>{
    public When(Class<? extends Callable<T>> aClass, Values values) {
        super(aClass, values);
    }
}
