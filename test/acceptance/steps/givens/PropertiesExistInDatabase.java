package acceptance.steps.givens;

import com.googlecode.propidle.properties.*;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;

import java.util.Properties;
import java.util.concurrent.Callable;

import static com.googlecode.propidle.properties.PropertyComparison.createdProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertiesExistInDatabase implements Callable<Void> {
    private final AllChanges allProperties;
    private PropertiesPath path;
    private Properties properties;

    public PropertiesExistInDatabase(AllChanges allProperties) {
        this.allProperties = allProperties;
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
        allProperties.put(com.googlecode.propidle.client.properties.Properties.toPairs(properties).map(toChange()));
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
