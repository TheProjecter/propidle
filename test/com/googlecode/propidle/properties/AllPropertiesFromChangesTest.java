package com.googlecode.propidle.properties;

import com.googlecode.lazyrecords.Records;
import com.googlecode.propidle.util.time.StoppedClock;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.ChangeDetail;
import com.googlecode.propidle.versioncontrol.changes.ChangeDetailsFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.HighestRevisionNumbers;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.time.Dates;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.UUID;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.properties.PropertyComparison.*;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import static com.googlecode.propidle.versioncontrol.changes.Changes.propertyNameOfChange;
import static com.googlecode.propidle.versioncontrol.revisions.NewRevisionNumber.newRevisionNumber;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllPropertiesFromChangesTest {
    private AllPropertiesFromChanges repository;
    private static final PropertiesPath PATH = propertiesPath(UUID.randomUUID().toString());
    private AllChanges changes;
    private HighestRevisionNumbers highestRevisionNumbers;
    private StoppedClock clock;
    private ChangeDetailsFromRecords changesDetails;

    @Before
    public void createRepository() {
        highestRevisionNumbers = mock(HighestRevisionNumbers.class);

        Records records = testRecordsWithAllMigrationsRun();
        changes = new AllChangesFromRecords(records);
        clock = new StoppedClock(Dates.date(1986, 1, 1));
        changesDetails = new ChangeDetailsFromRecords(records, clock);
        repository = new AllPropertiesFromChanges(changes, new PropertyDiffTool(), highestRevisionNumbers, changesDetails);
        givenRevisionIs(0);
    }

    @Test
    public void storesDateWithChange() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("NZL", "Number 1");

        repository.put(PATH, properties);

        Sequence<ChangeDetail> details = sequence(changesDetails.changesForRevision(revisionNumber(0))).realise();
        
        assertThat(details, IsCollectionContaining.hasItem(new ChangeDetail("date", clock.time().toString())));
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