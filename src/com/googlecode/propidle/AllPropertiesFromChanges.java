package com.googlecode.propidle;

import static com.googlecode.propidle.PropertyDiffTool.propertyValueChanged;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.Changes;
import static com.googlecode.propidle.versioncontrol.changes.Changes.properties;
import static com.googlecode.propidle.versioncontrol.changes.Changes.changes;
import static com.googlecode.propidle.versioncontrol.changes.Changes.revisionNumberOfChange;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.totallylazy.Predicate;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.where;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.util.Properties;

public class AllPropertiesFromChanges implements AllProperties {
    private final AllChanges changes;
    private final PropertyDiffTool propertyDiffTool;
    private final HighestRevisionNumbers highestRevisionNumbers;

    public AllPropertiesFromChanges(AllChanges changes, PropertyDiffTool propertyDiffTool, HighestRevisionNumbers highestRevisionNumbers) {
        this.changes = changes;
        this.propertyDiffTool = propertyDiffTool;
        this.highestRevisionNumbers = highestRevisionNumbers;
    }

    public Properties get(PropertiesPath path, RevisionNumber revision) {
        if(revision==null)throw new NullArgumentException("revision");
        return properties(getChanges(path, where(revisionNumberOfChange(), lessThanOrEqualTo(revision))));
    }

    public RevisionNumber put(PropertiesPath path, Properties updated) {
        NewRevisionNumber newRevisionNumber = highestRevisionNumbers.newRevisionNumber();
        Properties previous = get(path, newRevisionNumber.minus(1));

        changes.put(changes(path, newRevisionNumber, diff(previous, updated)));

        return newRevisionNumber;
    }

    private Sequence<PropertyComparison> diff(Properties previous, Properties updated) {
        Sequence<PropertyComparison> diffs = sequence(propertyDiffTool.diffs(previous, updated));
        return diffs.filter(propertyValueChanged());
    }

    private Sequence<Change> getChanges(PropertiesPath path, Predicate<? super Change> predicate) {
        return sequence(changes.get(path)).filter(predicate);
    }

}
