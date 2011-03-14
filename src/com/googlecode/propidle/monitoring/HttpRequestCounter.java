package com.googlecode.propidle.monitoring;

import java.util.concurrent.atomic.AtomicLong;

public class HttpRequestCounter implements HttpRequestCounterMBean {
    private AtomicLong count = new AtomicLong();

    public HttpRequestCounter() {
        count.set(0);
    }

    public void increment() {
        count.getAndIncrement();

    }

    public AtomicLong getCount() {
        return count;
    }
}
    