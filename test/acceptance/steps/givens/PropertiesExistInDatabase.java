package acceptance.steps.givens;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.Application;
import com.googlecode.yadic.Container;

import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.Properties.toPairs;
import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;

public class PropertiesExistInDatabase implements Callable<Void> {
    private final Application application;
    private PropertiesPath path;
    private Properties properties;

    public PropertiesExistInDatabase(Application application) {
        this.application = application;
    }

    public PropertiesExistInDatabase with(PropertiesPath path) {
        this.path = path;
        return this;
    }

    public PropertiesExistInDatabase and(Properties properties) {
        this.properties = properties;
        return this;
    }

    public Void call() throws Exception {
        application.usingRequestScope(new Block<Container>() {
            @Override
            protected void execute(Container container) throws Exception {
                final AllChanges allChanges = container.get(AllChanges.class);
                allChanges.put(toPairs(properties).map(toChange()));
                container.get(Connection.class).commit();
            }
        });
        return null;
    }

    private Callable1<Pair<String, String>, Change> toChange() {
        return new Callable1<Pair<String, String>, Change>() {
            public Change call(Pair<String, String> propertyPair) throws Exception {
                return Change.change(anyRevisionNumber(), path, createdProperty(propertyName(propertyPair.first()), propertyValue(propertyPair.second())));
            }
        };
    }

    private RevisionNumber anyRevisionNumber() {
        return RevisionNumber.revisionNumber(1);
    }
}
