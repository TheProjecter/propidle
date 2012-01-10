package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import static java.lang.String.format;
import static java.lang.String.valueOf;

public class ScheduleTaskRequest implements ScheduleTask {
    private final Application application;

    public ScheduleTaskRequest(Application application) {
        this.application = application;
    }

    public void schedule(final String taskName, long initialDelay, long delay) throws Exception {
        Response response = application.handle(RequestBuilder.post(ScheduleResource.NAME).
                withForm(ScheduleResource.TASK_NAME_PARAM_NAME, taskName).
                withForm(ScheduleResource.INITIAL_DELAY_IN_SECONDS_PARAM_NAME, valueOf(initialDelay)).
                withForm(ScheduleResource.DELAY_IN_SECONDS_PARAM_NAME, valueOf(delay)).
                build());

        if(response.status() == Status.OK) {
            System.out.println(taskName + " successfully scheduled");
        } else {
            throw new RuntimeException(format("Scheduling %s failed with code " + response.status().code(), taskName));
        }
    }
    
    public void schedule(final String taskName, long delay) throws Exception {
        schedule(taskName, 0, delay);
    }
}
