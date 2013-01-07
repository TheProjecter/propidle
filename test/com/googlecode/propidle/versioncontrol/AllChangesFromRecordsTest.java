package com.googlecode.propidle.versioncontrol;

import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.propidle.versioncontrol.changes.ChildPathsFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.propidle.PathType.DIRECTORY;
import static com.googlecode.propidle.PathType.FILE;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyComparison.*;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.util.matchers.HasInAnyOrder.hasInAnyOrder;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AllChangesFromRecordsTest {
    private final Records records = testRecordsWithAllMigrationsRun();
    private final AllChangesFromRecords changes = new AllChangesFromRecords(records, new ChildPathsFromRecords(records));
    private final RevisionNumber someRevisionNumber = revisionNumber(3242);

    @Test
    public void shouldBeAbleToGetChangesByPropertiesPath() {
        PropertiesPath propertiesPath = propertiesPath("/properties/production");

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
        PropertiesPath propertiesPath = propertiesPath("/properties/production");

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

    @Test
    public void shouldBeAbleToGetChildrenOfPath() {

        changes.put(createChangesWith(propertiesPath("/myapp/production")));
        changes.put(createChangesWith(propertiesPath("/myapp/test")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/test")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/my/apps1/properties")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/my/apps2/properties")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/my")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/aaaa")));
        changes.put(createChangesWith(propertiesPath("/myapp/test/easy/like/what")));
        changes.put(createChangesWith(propertiesPath("/myapp")));

        Sequence<Pair<PropertiesPath, PathType>> children = sequence(changes.childrenOf(propertiesPath("/myapp/test")));
        
        assertThat(children, is(hasInAnyOrder(
                pair(propertiesPath("/myapp/test/my"), DIRECTORY),
                pair(propertiesPath("/myapp/test/my"), FILE),
                pair(propertiesPath("/myapp/test/test"), FILE),
                pair(propertiesPath("/myapp/test/aaaa"), FILE),
                pair(propertiesPath("/myapp/test/easy"), DIRECTORY))));

    }

    private Sequence<Change> createChangesWith(PropertiesPath propertiesPath) {
        return sequence(
                    change(
                            someRevisionNumber,
                            propertiesPath,
                            createdProperty(
                                    propertyName("number.of.pigs"),
                                    propertyValue("42")
                            )),
                       change(
                            someRevisionNumber,
                            propertiesPath,
                            createdProperty(
                                    propertyName("number.of.sausages"),
                                    propertyValue("11554")
                            )),
                change(
                            someRevisionNumber,
                            propertiesPath,
                            createdProperty(
                                    propertyName("number.of.sausage.candidates"),
                                    propertyValue("42")
                            )));
    }
}
