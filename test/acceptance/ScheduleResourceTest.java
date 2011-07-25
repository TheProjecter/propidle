package acceptance;

import acceptance.steps.givens.TaskIsScheduled;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.propidle.scheduling.RunnableRequest;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import org.junit.Test;

import java.util.concurrent.Callable;

import static acceptance.TestSupportModule.SCHEDULED_TEST_TASK_NAME;
import static acceptance.TestSupportModule.SCHEDULED_TEST_URL;
import static acceptance.steps.thens.LastResponse.theStatusOf;
import static com.googlecode.propidle.scheduling.ScheduleResource.TASK_NAME_PARAM_NAME;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.*;
import static org.hamcrest.Matchers.is;

public class ScheduleResourceTest extends PropertiesApplicationTestCase {

    private static final String TASK_NAME = TASK_NAME_PARAM_NAME;

    @Test
    public void cannotScheduleAnUnknownTask() throws Exception {
        when(a(RequestIsMade.class).to(post("/schedule").withForm(TASK_NAME, "unknownTask")));

        then(theStatusOf(), the(LastResponse.class), is(NOT_FOUND));
    }

    @Test
    public void scheduleKnownTask() throws Exception {
        when(a(RequestIsMade.class).to(post("/schedule").withForm(TASK_NAME, SCHEDULED_TEST_TASK_NAME)));

        then(theStatusOf(), the(LastResponse.class), is(OK));
        then(theScheduledRequestsPath(), is(SCHEDULED_TEST_URL));
    }

    @Test
    public void doNotScheduleTaskAlreadyScheduled() throws Exception {
        given(a(TaskIsScheduled.class).withName(SCHEDULED_TEST_TASK_NAME));

        when(a(RequestIsMade.class).to(post("/schedule").withForm(TASK_NAME, SCHEDULED_TEST_TASK_NAME)));

        then(theStatusOf(), the(LastResponse.class), is(CONFLICT));
    }

    private Callable<HierarchicalPath> theScheduledRequestsPath() {
        return new Callable<HierarchicalPath>() {
            public HierarchicalPath call() throws Exception {
                return ((RunnableRequest)executorService.scheduledTask()).request().url().path();
            }
        };
    }


}
