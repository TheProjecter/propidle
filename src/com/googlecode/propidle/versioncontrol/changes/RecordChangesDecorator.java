package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyDiffTool;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.totallylazy.Sequence;

import java.util.Properties;

import static com.googlecode.propidle.PropertyDiffTool.propertyValueChanged;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

public class RecordChangesDecorator implements AllProperties {
    private final AllProperties decorated;
    private final Changes changes;
    private final PropertyDiffTool propertyDiffTool;

    public RecordChangesDecorator(AllProperties decorated, Changes changes, PropertyDiffTool propertyDiffTool) {
        this.decorated = decorated;
        this.changes = changes;
        this.propertyDiffTool = propertyDiffTool;
    }

    public Properties get(PropertiesPath path) {
        return decorated.get(path);
    }

    public AllProperties put(PropertiesPath path, Properties updated) {
        Properties previous = decorated.get(path);
        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(previous, updated));

        decorated.put(path, updated);
        changes.put(path, diffs.filter(propertyValueChanged()));

        return this;
    }

}
