package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Request;

public class SchedulableTask {
    private final String name;
    private final Request request;

    public SchedulableTask(String name, Request request) {
        this.name = name;
        this.request = request;
    }

    public String name() {
        return name;
    }

    public Request request() {
        return request;
    }

}
