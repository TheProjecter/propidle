package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class PropertyReloadScheduler implements Callable1<PropertyValue, Void> {
    private final ScheduleTaskRequest scheduleTaskRequest;

    public PropertyReloadScheduler(ScheduleTaskRequest scheduleTaskRequest) {
        this.scheduleTaskRequest = scheduleTaskRequest;
    }

    public Void call(final PropertyValue propertyValue) throws Exception {
        scheduleTaskRequest.send("reloadProperties", Long.valueOf(propertyValue.value()));
        return null;
    }


}
