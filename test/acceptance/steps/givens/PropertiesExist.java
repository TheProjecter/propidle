package acceptance.steps.givens;

import com.googlecode.propidle.properties.AllProperties;
import com.googlecode.propidle.properties.PropertiesPath;

import java.util.Properties;
import java.util.concurrent.Callable;

public class PropertiesExist implements Callable<Properties> {
    private final AllProperties allProperties;
    private PropertiesPath path;
    private Properties properties;

    public PropertiesExist(AllProperties allProperties) {
        this.allProperties = allProperties;
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
        allProperties.put(path, properties);
        return properties;
    }
}
