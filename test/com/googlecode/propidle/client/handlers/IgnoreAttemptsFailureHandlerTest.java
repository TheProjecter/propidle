package com.googlecode.propidle.client.handlers;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Callers;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.totallylazy.Sequences.repeat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IgnoreAttemptsFailureHandlerTest {
    @Test
    public void shouldFailAfterAttempts() throws Exception {
        int ignores = 3;
        int attempts = 8;
        CountingFailureHandler counter = new CountingFailureHandler();
        final IgnoreAttemptsFailureHandler handler = new IgnoreAttemptsFailureHandler(ignores, counter);
        repeat(new Exception("Hello")).take(attempts).eachConcurrently(new Block<Exception>() {
            @Override
            protected void execute(Exception e) throws Exception {
                handler.handle(e);
            }
        });

        assertThat(counter.calls(), is(attempts / (ignores + 1)));
    }

    private static class CountingFailureHandler implements FailureHandler {
        private final AtomicInteger calls = new AtomicInteger(0);

        @Override
        public void handle(Exception exception) {
            calls.incrementAndGet();
        }

        private int calls() {
            return calls.get();
        }
    }
}
