package com.googlecode.propidle.util;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ParallelExecutionGuardTest {

    private final AtomicInteger executeCounter = new AtomicInteger(0);
    private final ParallelExecutionGuard parallelExecutionGuard = new ParallelExecutionGuard();
    private final CountDownLatch stop = new CountDownLatch(1);
    private final CountDownLatch start = new CountDownLatch(1);

    @Test
    public void shouldExecuteAndReturnTrueWhenNoOneIsDoingWork() {
        assertThat(parallelExecutionGuard.execute(service()), is(true));
        assertThat(parallelExecutionGuard.execute(service()), is(true));
        assertThat(executeCounter.get(), is(2));
    }

    @Test
    public void shouldReturnFalseWhenExecutionIsInProgress() throws InterruptedException {
        Thread slowService = new Thread(new ExecuteSlowService());

        startExecuting(slowService);
        assertThat(parallelExecutionGuard.execute(service()), is(false));

        waitToFinish(slowService);
        assertThat(executeCounter.get(), is(1));
    }

    private void waitToFinish(Thread slowService) throws InterruptedException {
        stop.countDown();
        slowService.join();
    }

    private void startExecuting(Thread slowServiceExecution) throws InterruptedException {
        slowServiceExecution.start();
        start.await();
    }


    private class ExecuteSlowService implements Runnable {
        public void run() {
            parallelExecutionGuard.execute(new Callable<Void>() {
                public Void call() throws Exception {
                    start.countDown();
                    executeCounter.incrementAndGet();
                    stop.await();
                    return null;
                }
            });
        }
    }

    private Callable<Void> service() {
        return new Callable<Void>(){
            public Void call() throws Exception {
                executeCounter.incrementAndGet();
                return null;
            }
        };
    }

}
