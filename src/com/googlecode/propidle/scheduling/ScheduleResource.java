package com.googlecode.propidle.scheduling;

import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.annotations.FormParam;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.ResponseBuilder.response;
import static com.googlecode.utterlyidle.Status.NOT_FOUND;
import static com.googlecode.utterlyidle.Status.OK;
import static java.util.concurrent.TimeUnit.SECONDS;

@Path(ScheduleResource.NAME)
@Produces(TEXT_HTML)
public class ScheduleResource {

    public static final String TASK_NAME_PARAM_NAME = "taskName";
    public static final String DELAY_IN_SECONDS_PARAM_NAME = "delayInSeconds";
    public static final String INITIAL_DELAY_IN_SECONDS_PARAM_NAME = "initialDelayInSeconds";
    private final Scheduler scheduler;
    private final SchedulableRequests requests;
    public static final String NAME = "/schedule";

    public ScheduleResource(Scheduler scheduler, SchedulableRequests requests) {
        this.scheduler = scheduler;
        this.requests = requests;
    }

    @POST
    public Response schedule(@FormParam(TASK_NAME_PARAM_NAME) String taskName, @FormParam(DELAY_IN_SECONDS_PARAM_NAME) Long delayInSeconds, @FormParam(INITIAL_DELAY_IN_SECONDS_PARAM_NAME) Option<Long> initialDelayInSecondsOption) {
        RunnableRequest runnableRequest = requests.runnableRequest(taskName);
        if(runnableRequest == null) {
            return response(NOT_FOUND).entity("Could not schedule unknown tasks. Available tasks are: " + sequence(requests.availableTaskNames()).toString()).build();
        }
        scheduler.schedule(runnableRequest, initialDelayInSecondsOption.getOrElse(0L), delayInSeconds, SECONDS);

        return response(OK).entity("Task has been scheduled").build();

    }

}
