package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

public class RebuildIndexScheduler implements Callable1<PropertyValue, Void> {
    private final Application application;

    public RebuildIndexScheduler(Application application) {
        this.application = application;
    }

    public Void call(PropertyValue propertyValue) throws Exception {
        Response response = application.handle(RequestBuilder.post(ScheduleResource.NAME).
                withForm(ScheduleResource.TASK_NAME_PARAM_NAME, "rebuildIndex").
                withForm(ScheduleResource.DELAY_IN_SECONDS_PARAM_NAME, propertyValue.value()).build());

        if(response.status() == Status.OK) {
            System.out.println("Re-indexing successfully scheduled");
        } else {
            System.err.println("Scheduling re-indexing failed with code " + response.status().code());
        }
        return null;
    }

}
