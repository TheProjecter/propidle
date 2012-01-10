package acceptance;

import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.propidle.scheduling.RunnableRequest;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import org.junit.Test;

import java.util.concurrent.Callable;

import static acceptance.TestSupportModule.SCHEDULED_TEST_TASK_NAME;
import static acceptance.TestSupportModule.SCHEDULED_TEST_URL;
import static acceptance.steps.thens.LastResponse.theStatusOf;
import static com.googlecode.propidle.scheduling.ScheduleResource.DELAY_IN_SECONDS_PARAM_NAME;
import static com.googlecode.propidle.scheduling.ScheduleResource.INITIAL_DELAY_IN_SECONDS_PARAM_NAME;
import static com.googlecode.propidle.scheduling.ScheduleResource.TASK_NAME_PARAM_NAME;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.*;
import static org.hamcrest.Matchers.is;

public class ScheduleResourceTest extends PropertiesApplicationTestCase {

    private static final String DELAY = "10";

    @Test
    public void cannotScheduleAnUnknownTask() throws Exception {
        when(a(RequestIsMade.class).to(post("/schedule").
                withForm(TASK_NAME_PARAM_NAME, "unknownTask").
                withForm(DELAY_IN_SECONDS_PARAM_NAME, DELAY)));

        then(theStatusOf(), the(LastResponse.class), is(NOT_FOUND));
    }

    @Test
    public void scheduleKnownTask() throws Exception {
        when(a(RequestIsMade.class).to(post("/schedule").
                withForm(TASK_NAME_PARAM_NAME, SCHEDULED_TEST_TASK_NAME).
                withForm(DELAY_IN_SECONDS_PARAM_NAME, DELAY)));

        then(theStatusOf(), the(LastResponse.class), is(OK));
        then(theScheduledRequestsPath(), is(SCHEDULED_TEST_URL));
    }

    @Test
    public void scheduleKnownTaskWithInitialDelay() throws Exception {
        when(a(RequestIsMade.class).to(post("/schedule").
                withForm(TASK_NAME_PARAM_NAME, SCHEDULED_TEST_TASK_NAME).
                withForm(DELAY_IN_SECONDS_PARAM_NAME, DELAY).
                withForm(INITIAL_DELAY_IN_SECONDS_PARAM_NAME, DELAY)));

        then(theStatusOf(), the(LastResponse.class), is(OK));
        then(theScheduledRequestsPath(), is(SCHEDULED_TEST_URL));
    }

    private Callable<String> theScheduledRequestsPath() {
        return new Callable<String>() {
            public String call() throws Exception {
                return ((RunnableRequest) executorService.scheduledTask()).request().uri().path();
            }
        };
    }


}
