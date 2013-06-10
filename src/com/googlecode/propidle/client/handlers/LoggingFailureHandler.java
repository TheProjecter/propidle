package com.googlecode.propidle.client.handlers;

import com.googlecode.propidle.client.logging.Logger;

import static com.googlecode.propidle.client.logging.Message.message;
import static com.googlecode.propidle.util.Exceptions.stackTraceToString;

public class LoggingFailureHandler implements FailureHandler {
    private final Logger logger;

    public LoggingFailureHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(Exception exception) {
        logger.log(message(stackTraceToString(exception)));
    }
}
