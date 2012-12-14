package acceptance.steps.givens;

import com.googlecode.propidle.scheduling.ScheduleResource;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.Response;

import java.util.concurrent.Callable;

import static com.googlecode.propidle.scheduling.ScheduleResource.TASK_NAME_PARAM_NAME;

public class TaskIsScheduled implements Callable<Response> {
    private String scheduledTaskName;
    private final Application application;

    public TaskIsScheduled(Application application) {
        this.application = application;
    }

    public TaskIsScheduled withName(String scheduledTestTaskName) {
        this.scheduledTaskName = scheduledTestTaskName;
        return this;
    }

    public Response call() throws Exception {
        return application.handle(RequestBuilder.post(ScheduleResource.NAME).form(TASK_NAME_PARAM_NAME, scheduledTaskName).build());
    }
}
