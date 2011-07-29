package com.googlecode.propidle.scheduling;

import java.util.concurrent.TimeUnit;

public class Delay {
    private final long duration;
    private final TimeUnit timeUnit;

    public Delay(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long duration() {
        return duration;
    }

    public TimeUnit timeUnit() {
        return timeUnit;
    }
}
