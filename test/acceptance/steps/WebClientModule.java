package acceptance.steps;

import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.yadic.Container;

public class WebClientModule implements ApplicationScopedModule {
    public Module addPerApplicationObjects(Container container) {
        container.add(WebClient.class);
        return this;
    }
}
