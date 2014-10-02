package acceptance.steps.givens;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Block;
import com.googlecode.utterlyidle.Application;
import com.googlecode.yadic.Container;

import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.Callable;

public class PropertiesExist implements Callable<Properties> {
    private PropertiesPath path;
    private Properties properties;
    private final Application application;

    public PropertiesExist(Application application) {
        this.application = application;
    }

    public PropertiesExist with(PropertiesPath path) {
        this.path = path;
        return this;
    }

    public PropertiesExist and(Properties properties) {
        this.properties = properties;
        return this;
    }

    public Properties call() throws Exception {
        application.usingRequestScope(new Block<Container>() {
            @Override
            protected void execute(Container container) throws Exception {
                final AllProperties allProperties = container.get(AllProperties.class);
                allProperties.put(path, properties);
                container.get(Connection.class).commit();
            }
        });
        return properties;
    }
}
