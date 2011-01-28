package com.googlecode.propidle;

import com.googlecode.propidle.versioncontrol.changes.Changes;
import com.googlecode.propidle.versioncontrol.changes.ChangesFromRecords;
import com.googlecode.propidle.versioncontrol.revisions.CurrentRevisionNumber;
import com.googlecode.totallylazy.records.Records;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.UUID;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static com.googlecode.propidle.versioncontrol.changes.ChangesFromRecords.defineChangesRecord;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllPropertiesFromChangesTest {
    private AllPropertiesFromChanges repository;
    private static final PropertiesPath PATH = propertiesPath(UUID.randomUUID().toString());
    private Changes changes;
    private CurrentRevisionNumber currentRevisionNumber;
    private Records records;

    @Before
    public void createRepository() throws Exception {
        currentRevisionNumber = mock(CurrentRevisionNumber.class);
        records = defineChangesRecord(temporaryRecords());
        changes = new ChangesFromRecords(records, currentRevisionNumber);
        repository = new AllPropertiesFromChanges(changes, new PropertyDiffTool());
        givenRevisionIs(0);
    }

    @Test
    public void canGetPropertiesByPath() {
        Properties properties = new Properties();
        properties.setProperty("spang", "whoopit");

        repository.put(PATH, properties);

        assertThat(repository.get(PATH), is(properties));
    }

    @Test
    public void willDeletePropertiesWhenTheyAreRemoved() {
        Properties original = new Properties();
        original.setProperty("will.be.deleted", "whoopit");

        repository.put(PATH, original);

        Properties updated = new Properties();
        updated.setProperty("new.property", "spang");
        repository.put(PATH, updated);

        assertThat(repository.get(PATH), is(updated));
    }

    @Test
    public void willReturnEmptyPropertiesObjectIfPathDoesNotExist() {
        assertThat(repository.get(PATH), is(new Properties()));
    }

    @Test
    public void whenWePutEmptyPropertiesNothingHappens() {
        Properties properties = new Properties();

        repository.put(PATH, properties);

        assertThat(repository.get(PATH), is(properties));
    }

    @Test
    public void canRetrievePropertiesAtAGivenRevision() throws Exception{
        givenRevisionIs(0);
        repository.put(PATH, properties("a=1\nb=2"));
        givenRevisionIs(1);
        repository.put(PATH, properties("a=2\nc=3"));
        
        assertThat(repository.getAtRevision(PATH, revisionNumber(0)), is(properties("a=1\nb=2")));
    }

//    @Test
//    public void shouldSaveChangesBetweenPropertyFiles() {
//        Sequence<PropertyComparison> changesFromDiffTool = sequence(
//                newProperty(propertyName("moomin.toes"), propertyValue("eels")),
//                removedProperty(propertyName("embalming.pingu"), propertyValue("flange")),
//                changedProperty(propertyName("spice.grills"), propertyValue("comedy"), propertyValue("value")));
//
//        when(allProperties.get(PATH)).thenReturn(previous);
//        when(diffTool.diffs(previous, updated)).thenReturn(changesFromDiffTool);
//
//        recordChangesDecorator.put(PATH, updated);
//
//        verify(changes, times(1)).put(argThatIs(PATH), argThatHasChanges(changesFromDiffTool));
//    }
//
//    @Test
//    public void shouldNotSaveChangesForUnchangedProperties() {
//        PropertyComparison unchangedProperty = unchangedProperty(propertyName("expanding.bolognese"), propertyValue("stoogies"));
//        Sequence<PropertyComparison> changesFromDiffTool = sequence(
//                newProperty(propertyName("moomin.toes"), propertyValue("eels")),
//                removedProperty(propertyName("simple.tentacles"), propertyValue("doom")),
//                changedProperty(propertyName("stagnating.wimple"), propertyValue("fancy a stick?"), propertyValue("sure, whatever")),
//                unchangedProperty
//        );
//
//        when(allProperties.get(PATH)).thenReturn(previous);
//        when(diffTool.diffs(previous, updated)).thenReturn(changesFromDiffTool);
//
//        recordChangesDecorator.put(PATH, updated);
//
//        verify(changes, times(1)).put(argThatIs(PATH), argThatHasChanges(sequence(changesFromDiffTool.remove(unchangedProperty))));
//    }
//
//    private static PropertiesPath argThatIs(PropertiesPath path) {
//        return (PropertiesPath) argThat(is(path));
//    }
//
//    private Iterable<PropertyComparison> argThatHasChanges(Sequence<PropertyComparison> changes) {
//        return (Iterable<PropertyComparison>) argThat(hasExactly(changes));
//    }
    private void givenRevisionIs(int revision) {
        when(currentRevisionNumber.current()).thenReturn(revisionNumber(revision));
    }
}