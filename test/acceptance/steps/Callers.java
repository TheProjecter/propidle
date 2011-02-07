package acceptance.steps;

import java.util.concurrent.Callable;

public class Callers {
    public static <T> T ensure(Callable<T> callable) throws Exception {
        return callable.call();
    }
}
