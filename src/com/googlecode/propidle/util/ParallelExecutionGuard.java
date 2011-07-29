package com.googlecode.propidle.util;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.Callers.call;

public class ParallelExecutionGuard {

    private AtomicBoolean executing = new AtomicBoolean(false);

    public boolean execute(Callable<Void> service) {
        if(!executing.compareAndSet(false, true)) {
            return false;
        }

        call(service);
        executing.set(false);
        return true;
    }
}
