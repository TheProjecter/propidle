package com.googlecode.propidle.versioncontrol;

import static com.googlecode.propidle.diff.PropertyComparison.*;
import com.googlecode.propidle.properties.PropertiesPath;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.util.matchers.HasInAnyOrder.hasInAnyOrder;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class AllChangesFromRecordsTest {
    private final AllChangesFromRecords changes = new AllChangesFromRecords(testRecordsWithAllMigrationsRun());

    @Test
    public void shouldBeAbleToGetChangesByPropertiesPath() {
        PropertiesPath propertiesPath = PropertiesPath.propertiesPath("/properties/production");

        Sequence<Change> firstSetOfExpectedChanges = sequence(
                change(
                        revisionNumber(0),
                        propertiesPath,
                        createdProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42")
                        )),
                change(
                        revisionNumber(0),
                        propertiesPath,
                        createdProperty(
                                propertyName("bird.anger.level"),
                                propertyValue("mild")
                        )));

        // Expect an update to version ids after the second set of changes for the same property id
        Sequence<Change> secondSetOfExpectedChanges = sequence(
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
                        )));

        changes.put(firstSetOfExpectedChanges);
        changes.put(secondSetOfExpectedChanges);

        assertThat(changes.get(propertiesPath), hasInAnyOrder(firstSetOfExpectedChanges.join(secondSetOfExpectedChanges)));
    }

    @Test
    public void shouldBeAbleToGetChangesByPropertiesPathAndRevisionNumber() {
        PropertiesPath propertiesPath = PropertiesPath.propertiesPath("/properties/production");

        Sequence<Change> revision0 = sequence(
                change(
                        revisionNumber(0),
                        propertiesPath,
                        createdProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42")
                        )));

        // Expect an update to version ids after the second set of changes for the same property id
        Sequence<Change> revision1 = sequence(
                change(
                        revisionNumber(1),
                        propertiesPath,
                        changedProperty(
                                propertyName("number.of.pigs"),
                                propertyValue("42"),
                                propertyValue("999")
                        )));

        changes.put(revision0);
        changes.put(revision1);

        assertThat(changes.get(propertiesPath, revisionNumber(0)), hasInAnyOrder(revision0));
    }
}
