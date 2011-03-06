package com.googlecode.propidle.util;

public class Exceptions {
    public static Exception toException(Throwable throwable) throws Exception {
        if(throwable instanceof Error) throw (Error) throwable;
        return (Exception) throwable;
    }
}
