package com.googlecode.propidle;

import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.Changes;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.util.Properties;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertyDiffTool.propertyValueChanged;
import static com.googlecode.propidle.versioncontrol.changes.Change.applyChange;
import static com.googlecode.totallylazy.Predicates.*;
import static com.googlecode.totallylazy.Sequences.sequence;

public class AllPropertiesFromChanges implements AllProperties {
    private final Changes changes;
    private final PropertyDiffTool propertyDiffTool;

    public AllPropertiesFromChanges(Changes changes, PropertyDiffTool propertyDiffTool) {
        this.changes = changes;
        this.propertyDiffTool = propertyDiffTool;
    }

    public Properties get(PropertiesPath path) {
        return createPropertiesFileFromChanges(path);
    }

    public Properties getAtRevision(PropertiesPath path, RevisionNumber revision) {
        return createPropertiesFileFromChanges(path, where(revisionNumber(), lessThanOrEqualTo(revision)));
    }

    public AllProperties put(PropertiesPath path, Properties properties) {
        Properties previous = get(path);
        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(previous, properties));

        changes.put(path, diffs.filter(propertyValueChanged()));
        return this;
    }

    private Properties createPropertiesFileFromChanges(PropertiesPath path) {
        return createPropertiesFileFromChanges(path, always(Change.class));
    }

    private Properties createPropertiesFileFromChanges(PropertiesPath path, Predicate<? super Change> predicate) {
        final Sequence<Change> sequence = sequence(changes.get(path));
        final Sequence<Change> filtered = sequence.filter(predicate);
        return filtered.sortBy(revisionNumber()).fold(properties(), applyChange());
    }

    private Callable1<? super Change, RevisionNumber> revisionNumber() {
        return new Callable1<Change, RevisionNumber>() {
            public RevisionNumber call(Change change) throws Exception {
                return change.revisionNumber();
            }
        };
    }
}
