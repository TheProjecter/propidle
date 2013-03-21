package com.googlecode.propidle.migrations.util.time;

import java.util.Date;

public class StoppedClock implements Clock{
    private Date time;

    public static StoppedClock stoppedClock() {
        return clockStoppedAt(new Date());
    }

    public static StoppedClock clockStoppedAt(Date time) {
        return new StoppedClock(time);
    }

    public StoppedClock(Date time) {
        this.time = time;
    }

    public Date time() {
        return time;
    }
    public Date time(Date time){
        this.time = time;
        return time;
    }
}
