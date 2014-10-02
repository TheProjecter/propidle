package com.googlecode.propidle.properties;

import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.ChangeDetailsFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;

import java.util.Properties;

import static com.googlecode.propidle.properties.PropertyDiffTool.propertyValueChanged;
import static com.googlecode.propidle.versioncontrol.changes.Changes.*;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class AllPropertiesFromChanges implements AllProperties {
    private final AllChanges changes;
    private final PropertyDiffTool propertyDiffTool;
    private final HighestRevisionNumbers highestRevisionNumbers;
    private final ChangeDetailsFromRecords changeDetailsFromRecords;

    public AllPropertiesFromChanges(AllChanges changes, PropertyDiffTool propertyDiffTool, HighestRevisionNumbers highestRevisionNumbers, ChangeDetailsFromRecords changeDetailsFromRecords) {
        this.changes = changes;
        this.propertyDiffTool = propertyDiffTool;
        this.highestRevisionNumbers = highestRevisionNumbers;
        this.changeDetailsFromRecords = changeDetailsFromRecords;
    }

    @Override
    public Properties get(PropertiesPath path, Option<? extends RevisionNumber> revision) {
        if (revision.isEmpty()) {
            return properties(getLatestChanges(path));
        }
        return sortedProperties(getChanges(path, where(revisionNumberOfChange(), lessThanOrEqualTo(revision.get()))));
    }

    public RevisionNumber put(PropertiesPath path, Properties updated) {
        RevisionNumber revisionNumber = highestRevisionNumbers.newRevisionNumber();
        Properties previous = get(path, some(revisionNumber.minus(1)));
        changes.put(changes(path, revisionNumber, diff(previous, updated)));
        changeDetailsFromRecords.createDetails(revisionNumber);
        return revisionNumber;
    }

    private Sequence<PropertyComparison> diff(Properties previous, Properties updated) {
        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(previous, updated));
        return diffs.filter(propertyValueChanged());
    }

    private Sequence<Change> getChanges(PropertiesPath path, Predicate<? super Change> predicate) {
        return sequence(changes.get(path)).filter(predicate);
    }

    private Sequence<Change> getLatestChanges(PropertiesPath path) {
        return sequence(changes.getLatestChanges(path));
    }

}
