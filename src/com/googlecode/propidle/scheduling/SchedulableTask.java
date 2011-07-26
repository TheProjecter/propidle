package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.utterlyidle.Request;

public class SchedulableTask {
    private final String name;
    private final Request request;
    private final PropertyName propertyName;

    public SchedulableTask(String name, Request request, PropertyName propertyName) {
        this.name = name;
        this.request = request;
        this.propertyName = propertyName;
    }

    public String name() {
        return name;
    }

    public Request request() {
        return request;
    }

    public PropertyName propertyName() {
        return propertyName;
    }

}
