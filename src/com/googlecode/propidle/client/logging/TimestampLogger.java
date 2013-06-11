package com.googlecode.propidle.client.logging;

import com.googlecode.totallylazy.time.Clock;
import com.googlecode.totallylazy.time.Dates;

import static com.googlecode.propidle.client.logging.Message.message;

public class TimestampLogger implements Logger {
    private final Logger delegate;
    private final Clock clock;
    private final String format;

    private TimestampLogger(Logger delegate, Clock clock, String format) {
        this.delegate = delegate;
        this.clock = clock;
        this.format = format;
    }

    public static TimestampLogger timestampLogger(Logger delegate, Clock clock, String format) {
        return new TimestampLogger(delegate, clock, format);
    }

    public static TimestampLogger timestampLogger(Logger delegate, Clock clock) {
        return timestampLogger(delegate, clock, Dates.APACHE);
    }

    @Override
    public void log(Message message) {
        delegate.log(message(String.format("%s %s", Dates.format(format).format(clock.now()), message.toString())));
    }
}