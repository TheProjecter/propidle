package acceptance;

import acceptance.steps.WebClient;
import acceptance.steps.givens.*;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.propidle.scheduling.SchedulableTask;
import com.googlecode.propidle.scheduling.SchedulableTaskModule;
import com.googlecode.propidle.scheduling.SchedulableTasks;
import com.googlecode.propidle.scheduling.StubScheduledExecutorService;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;

import java.util.concurrent.ScheduledExecutorService;

import static com.googlecode.utterlyidle.io.HierarchicalPath.hierarchicalPath;

public class TestSupportModule implements ApplicationScopedModule, RequestScopedModule, SchedulableTaskModule {
    public static final String SCHEDULED_TEST_TASK_NAME = "scheduledTestTask";
    public static final HierarchicalPath SCHEDULED_TEST_URL = hierarchicalPath("scheduledTestUrl");

    private final TestLogger logger;
    private final InterestingGivens interestingGivens;
    private final CapturedInputAndOutputs capturedInputAndOutputs;
    private final StubScheduledExecutorService executorService;

    public TestSupportModule(TestLogger logger, InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs, StubScheduledExecutorService executorService) {
        this.logger = logger;
        this.interestingGivens = interestingGivens;
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        this.executorService = executorService;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(WebClient.class);
        container.addInstance(InterestingGivens.class, interestingGivens);
        container.addInstance(CapturedInputAndOutputs.class, capturedInputAndOutputs);
        container.addInstance(TestLogger.class, logger);
        container.remove(ScheduledExecutorService.class);
        container.addInstance(ScheduledExecutorService.class, executorService);

        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.add(AliasExists.class);
        container.add(CurrentRevision.class);
        container.add(PropertiesExist.class);
        container.add(TaskIsScheduled.class);
        container.add(LastResponse.class);
        container.add(RequestIsMade.class);
        container.add(PropertiesExistInDatabase.class);

        return this;
    }

    public void addTask(SchedulableTasks tasks) {
        tasks.addTask(new SchedulableTask(SCHEDULED_TEST_TASK_NAME, RequestBuilder.post(SCHEDULED_TEST_URL.toString()).build()));
    }
}
