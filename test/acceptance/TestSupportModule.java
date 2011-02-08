package acceptance;

import acceptance.steps.WebClient;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import acceptance.steps.givens.*;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.utterlyidle.BasePath;
import static com.googlecode.utterlyidle.BasePath.basePath;
import com.googlecode.yadic.Container;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.TestLogger;

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
        container.add(CurrentUser.class);
        container.add(MembersOf.class);
        container.add(PropertiesExist.class);
        container.add(UserDoesNotExist.class);
        container.add(AUserExists.class);
        container.add(UserGroupExists.class);
        container.add(UserGroupPermissions.class);
        container.add(UserPermissions.class);
        container.add(CurrentUserPermissions.class);

        container.add(LastResponse.class);
        container.add(RequestIsMade.class);


        container.remove(BasePath.class);
        container.addInstance(BasePath.class, basePath("/"));
        return this;
    }

}
