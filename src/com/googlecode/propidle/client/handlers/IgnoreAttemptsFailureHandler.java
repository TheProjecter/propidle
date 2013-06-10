package com.googlecode.propidle.client.handlers;

import java.util.concurrent.atomic.AtomicInteger;

public class IgnoreAttemptsFailureHandler implements FailureHandler {
    private final AtomicInteger count;
    private final int attempts;
    private final FailureHandler handler;

    public IgnoreAttemptsFailureHandler(int attempts, FailureHandler handler) {
        this.attempts = attempts;
        this.handler = handler;
        this.count = new AtomicInteger(0);
    }

    @Override
    public void handle(Exception exception) {
        while (true) {
            int current = count.get();
            int next = current + 1;
            if (next > attempts) {
                if (count.compareAndSet(current, 0)) {
                    handler.handle(exception);
                    return;
                }
            } else if (count.compareAndSet(current, next)) {
                return;
            }
        }
    }
}
