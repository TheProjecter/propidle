package com.googlecode.propidle.versioncontrol;

import com.googlecode.propidle.AllProperties;
import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.PropertyDiffTool;
import com.googlecode.propidle.versioncontrol.changes.Changes;
import com.googlecode.propidle.versioncontrol.changes.RecordChangesDecorator;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertyName.propertyName;
import static com.googlecode.propidle.PropertyValue.propertyValue;
import static com.googlecode.propidle.PropertyComparison.*;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class RecordChangesDecoratorTest {
    private static final PropertiesPath PATH = PropertiesPath.propertiesPath("some/properties/file");
    private final Properties previous = properties();
    private final Properties updated = properties();

    private final AllProperties allProperties = mock(AllProperties.class);
    private final PropertyDiffTool diffTool = mock(PropertyDiffTool.class);
    private final Changes changes = mock(Changes.class);
    private final RecordChangesDecorator recordChangesDecorator = new RecordChangesDecorator(allProperties, changes, diffTool);

    @Test
    public void shouldSaveChangesBetweenPropertyFiles() {
        Sequence<PropertyComparison> changesFromDiffTool = sequence(
                newProperty(propertyName("moomin.toes"), propertyValue("eels")),
                removedProperty(propertyName("embalming.pingu"), propertyValue("flange")),
                changedProperty(propertyName("spice.grills"), propertyValue("comedy"), propertyValue("value")));

        when(allProperties.get(PATH)).thenReturn(previous);
        when(diffTool.diffs(previous, updated)).thenReturn(changesFromDiffTool);

        recordChangesDecorator.put(PATH, updated);

        verify(changes, times(1)).put(argThatIs(PATH), argThatHasChanges(changesFromDiffTool));
    }

    @Test
    public void shouldNotSaveChangesForUnchangedProperties() {
        PropertyComparison unchangedProperty = unchangedProperty(propertyName("expanding.bolognese"), propertyValue("stoogies"));
        Sequence<PropertyComparison> changesFromDiffTool = sequence(
                newProperty(propertyName("moomin.toes"), propertyValue("eels")),
                removedProperty(propertyName("simple.tentacles"), propertyValue("doom")),
                changedProperty(propertyName("stagnating.wimple"), propertyValue("fancy a stick?"), propertyValue("sure, whatever")),
                unchangedProperty
        );

        when(allProperties.get(PATH)).thenReturn(previous);
        when(diffTool.diffs(previous, updated)).thenReturn(changesFromDiffTool);

        recordChangesDecorator.put(PATH, updated);

        verify(changes, times(1)).put(argThatIs(PATH), argThatHasChanges(sequence(changesFromDiffTool.remove(unchangedProperty))));
    }

    private static PropertiesPath argThatIs(PropertiesPath path) {
        return (PropertiesPath) argThat(is(path));
    }

    private Iterable<PropertyComparison> argThatHasChanges(Sequence<PropertyComparison> changes) {
        return (Iterable<PropertyComparison>) argThat(hasExactly(changes));
    }
}
