package com.googlecode.propidle;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.currentThread;

public class ApplicationStartupBarrier {

    private final CountDownLatch startLatch;

    public ApplicationStartupBarrier(CountDownLatch startLatch) {
        this.startLatch = startLatch;
    }

    public void waitUntilAppIStartUp() {
        try {
            startLatch.await();
        } catch (InterruptedException e) {
            currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
