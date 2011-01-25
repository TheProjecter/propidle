package com.googlecode.propidle.server.sessions;

import com.googlecode.propidle.util.DateTinyType;
import com.googlecode.propidle.util.Clock;

import java.util.Date;

public class StartTime extends DateTinyType<StartTime> {
    public static StartTime startTime(Clock clock) {
        return startTime(clock.currentTime());
    }

    public static StartTime startTime(Date value) {
        return new StartTime(value);
    }

    protected StartTime(Date value) {
        super(value);
    }
}
