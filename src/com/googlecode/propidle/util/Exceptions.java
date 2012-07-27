package com.googlecode.propidle.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Exceptions {
    public static Exception toException(Throwable throwable) throws Exception {
        if(throwable instanceof Error) throw (Error) throwable;
        return (Exception) throwable;
    }

    public static String stackTraceToString(Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }
}
