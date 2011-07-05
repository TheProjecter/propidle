package acceptance;

import acceptance.steps.WebClient;
import acceptance.steps.givens.AliasExists;
import acceptance.steps.givens.CurrentRevision;
import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.utterlyidle.BasePath;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;

import static com.googlecode.utterlyidle.BasePath.basePath;

public class TestSupportModule implements ApplicationScopedModule, RequestScopedModule {
    private final TestLogger logger;
    private final InterestingGivens interestingGivens;
    private final CapturedInputAndOutputs capturedInputAndOutputs;

    public TestSupportModule(TestLogger logger, InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) {
        this.logger = logger;
        this.interestingGivens = interestingGivens;
        this.capturedInputAndOutputs = capturedInputAndOutputs;
    }

    public Module addPerApplicationObjects(Container container) {
        container.add(WebClient.class);
        container.addInstance(InterestingGivens.class, interestingGivens);
        container.addInstance(CapturedInputAndOutputs.class, capturedInputAndOutputs);
        container.addInstance(TestLogger.class, logger);
        return this;
    }

    public Module addPerRequestObjects(Container container) {
        container.add(AliasExists.class);
        container.add(CurrentRevision.class);
        container.add(PropertiesExist.class);

        container.add(LastResponse.class);
        container.add(RequestIsMade.class);

        return this;
    }

}
