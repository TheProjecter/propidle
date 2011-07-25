package com.googlecode.propidle.scheduling;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class StubScheduledExecutorService implements ScheduledExecutorService {
    private Runnable runnable;

    public ScheduledFuture<?> schedule(Runnable runnable, long l, TimeUnit timeUnit) {
        return null;
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> vCallable, long l, TimeUnit timeUnit) {
        return null;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        return null;
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long l, long l1, TimeUnit timeUnit) {
        this.runnable = runnable;
        return null;
    }

    public Runnable scheduledTask() {
        return runnable;
    }

    public void shutdown() {

    }

    public List<Runnable> shutdownNow() {
        return null;
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }

    public boolean awaitTermination(long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    public <T> Future<T> submit(Callable<T> tCallable) {
        return null;
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        return null;
    }

    public Future<?> submit(Runnable runnable) {
        return null;
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables) throws InterruptedException {
        return null;
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> callables, long l, TimeUnit timeUnit) throws InterruptedException {
        return null;
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> callables) throws InterruptedException, ExecutionException {
        return null;
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> callables, long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

    public void execute(Runnable runnable) {

    }
}
