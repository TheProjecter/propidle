package com.googlecode.propidle.versioncontrol;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.ChangesFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import static com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber.newRevisionNumber;
import org.junit.Test;

import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.changes.ChangesFromRecords.defineChangesRecord;
import static com.googlecode.propidle.PropertyComparison.newProperty;
import static com.googlecode.propidle.PropertyComparison.changedProperty;
import static com.googlecode.propidle.PropertyComparison.removedProperty;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChangesFromRecordsTest {
    private final HighestRevisionNumbers highestRevisionNumbers = mock(HighestRevisionNumbers.class);
    private final ChangesFromRecords changes = new ChangesFromRecords(defineChangesRecord(temporaryRecords()), highestRevisionNumbers);

    @Test
    public void shouldBeAbleToGetChangesByPropertiesPath() {
        PropertiesPath propertiesPath = PropertiesPath.propertiesPath("/properties/production");
        when(highestRevisionNumbers.newRevisionNumber()).thenReturn(
                newRevisionNumber(0),
                newRevisionNumber(1));

        Change[] firstSetOfExpectedChanges = new Change[]{
                change(
                        revisionNumber(0),
                        propertiesPath,
                        newProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42")
                        )),
                change(
                        revisionNumber(0),
                        propertiesPath,
                        newProperty(
                                propertyName("bird.anger.level"),
                                propertyValue("mild")
                        ))                };

        // Expect an update to version ids after the second set of changes for the same property id
        Change[] secondSetOfExpectedChanges = new Change[]{
                change(
                        revisionNumber(1),
                        propertiesPath,
                        changedProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42"),
                                propertyValue("999")
                        )),
                change(
                        revisionNumber(1),
                        propertiesPath,
                        removedProperty(
                                propertyName("bird.anger.level"),
                                propertyValue("enormous")
                        ))
                };

        changes.put(propertiesPath, changesFrom(firstSetOfExpectedChanges));
        changes.put(propertiesPath, changesFrom(secondSetOfExpectedChanges));

        assertThat(changes.get(propertiesPath), hasExactly(firstSetOfExpectedChanges[0], firstSetOfExpectedChanges[1], secondSetOfExpectedChanges[0], secondSetOfExpectedChanges[1]));
    }

    @Test
    public void shouldBeAbleToGetChangesByPropertiesPathAndRevisionNumber() {
        PropertiesPath propertiesPath = PropertiesPath.propertiesPath("/properties/production");
        when(highestRevisionNumbers.newRevisionNumber()).thenReturn(
                newRevisionNumber(0),
                newRevisionNumber(1));

        Change[] revision0 = new Change[]{
                change(
                        revisionNumber(0),
                        propertiesPath,
                        newProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42")
                        ))};

        // Expect an update to version ids after the second set of changes for the same property id
        Change[] revision1 = new Change[]{
                change(
                        revisionNumber(1),
                        propertiesPath,
                        changedProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42"),
                                propertyValue("999")
                        ))};

        changes.put(propertiesPath, changesFrom(revision0));
        changes.put(propertiesPath, changesFrom(revision1));

        assertThat(changes.get(propertiesPath, revisionNumber(0)), hasExactly(revision0[0]));
    }

    private Iterable<PropertyComparison> changesFrom(Change... changes) {
        return sequence(changes).map(method(on(Change.class).change()));
    }
}
