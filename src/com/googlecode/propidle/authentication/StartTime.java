package com.googlecode.propidle.authentication;

import com.googlecode.propidle.util.tinytype.DateTinyType;
import com.googlecode.propidle.util.time.Clock;

import java.util.Date;

public class StartTime extends DateTinyType<StartTime> {
    public static StartTime startTime(Clock clock) {
        return startTime(clock.time());
    }

    public static StartTime startTime(Date value) {
        return new StartTime(value);
    }

    protected StartTime(Date value) {
        super(value);
    }
}
