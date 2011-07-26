package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.FormParam;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.Responses.response;
import static com.googlecode.utterlyidle.Status.*;

@Path(ScheduleResource.NAME)
@Produces(TEXT_HTML)
public class ScheduleResource {

    public static final String TASK_NAME_PARAM_NAME = "taskName";
    private final Scheduler scheduler;
    private final SchedulableTasks tasks;
    public static final String NAME = "/schedule";

    public ScheduleResource(Scheduler scheduler, SchedulableTasks tasks) {
        this.scheduler = scheduler;
        this.tasks = tasks;
    }

    @POST
    public Response schedule(@FormParam(TASK_NAME_PARAM_NAME) String taskName) {
        SchedulableTask task = tasks.getTask(taskName);
        if(task == null) {
            return response(NOT_FOUND).entity("Could not schedule unknown tasks. Available tasks are: " + sequence(tasks.availableTaskNames()).toString());
        }

        scheduler.schedule(task);
        return response(OK).entity("Task has been scheduled");
    }

}
