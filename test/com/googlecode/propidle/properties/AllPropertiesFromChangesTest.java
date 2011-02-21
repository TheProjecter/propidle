package com.googlecode.propidle.properties;

import static com.googlecode.propidle.diff.PropertyComparison.*;
import com.googlecode.propidle.diff.PropertyDiffTool;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.changes.Changes.propertyNameOfChange;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import static com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber.newRevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import com.googlecode.totallylazy.records.Records;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.UUID;

public class AllPropertiesFromChangesTest {
    private AllPropertiesFromChanges repository;
    private static final PropertiesPath PATH = propertiesPath(UUID.randomUUID().toString());
    private AllChanges changes;
    private HighestRevisionNumbers highestRevisionNumbers;
    private Records records;

    @Before
    public void createRepository() throws Exception {
        highestRevisionNumbers = mock(HighestRevisionNumbers.class);
        records = testRecordsWithAllMigrationsRun();
        changes = new AllChangesFromRecords(records);
        repository = new AllPropertiesFromChanges(changes, new PropertyDiffTool(), highestRevisionNumbers);
        givenRevisionIs(0);
    }

    @Test
    public void canGetPropertiesByPath() {
        Properties properties = new Properties();
        properties.setProperty("spang", "whoopit");

        repository.put(PATH, properties);

        assertThat(repository.get(PATH, revisionNumber(0)), is(properties));
    }

    @Test
    public void willDeletePropertiesWhenTheyAreRemoved() {
        givenRevisionIs(0);
        Properties original = properties("will.be.deleted=whoopit");
        repository.put(PATH, original);

        givenRevisionIs(1);
        Properties updated = properties("new.property=spang");
        repository.put(PATH, updated);

        assertThat(repository.get(PATH, revisionNumber(1)), is(updated));
    }

    @Test
    public void willReturnEmptyPropertiesObjectIfPathDoesNotExist() {
        assertThat(repository.get(PATH, revisionNumber(0)), is(new Properties()));
    }

    @Test
    public void whenWePutEmptyPropertiesNothingHappens() {
        Properties properties = new Properties();

        repository.put(PATH, properties);

        assertThat(repository.get(PATH, revisionNumber(0)), is(properties));
    }

    @Test
    public void canRetrievePropertiesAtAGivenRevision() throws Exception {
        givenRevisionIs(0);
        repository.put(PATH, properties("a=1\nb=2"));
        givenRevisionIs(1);
        repository.put(PATH, properties("a=2\nc=3"));

        assertThat(repository.get(PATH, revisionNumber(0)), is(properties("a=1\nb=2")));
    }

    @Test
    public void shouldSaveChangesBetweenPropertyFiles() {
        givenRevisionIs(0);
        repository.put(PATH, properties("a=1\nb=old\nc=3"));
        givenRevisionIs(1);
        repository.put(PATH, properties("a=1\nb=new\nd=4"));

        assertThat(sequence(changes.get(PATH, revisionNumber(1))).sortBy(propertyNameOfChange()),
                   hasExactly(
                           change(revisionNumber(1), PATH, changedProperty(propertyName("b"), propertyValue("old"), propertyValue("new"))),
                           change(revisionNumber(1), PATH, removedProperty(propertyName("c"), propertyValue("3"))),
                           change(revisionNumber(1), PATH, createdProperty(propertyName("d"), propertyValue("4")))));
    }

    private void givenRevisionIs(int revision) {
        when(highestRevisionNumbers.newRevisionNumber()).thenReturn(newRevisionNumber(revision));
    }
}