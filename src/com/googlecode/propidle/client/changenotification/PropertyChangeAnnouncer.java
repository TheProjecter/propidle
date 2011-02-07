package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.diff.PropertyComparison;
import com.googlecode.propidle.diff.PropertyDiffTool;
import com.googlecode.totallylazy.Sequence;
import org.jmock.example.announcer.Announcer;

import java.util.Properties;

import static com.googlecode.propidle.diff.PropertyDiffTool.propertyValueChanged;
import static com.googlecode.propidle.client.changenotification.PropertyChangeEvent.propertyChangeEvent;
import com.googlecode.propidle.client.PropertyChangeNotifier;
import com.googlecode.propidle.client.PropertyChangeListener;
import static com.googlecode.totallylazy.Sequences.sequence;

public class PropertyChangeAnnouncer implements PropertyChangeNotifier {
    private final Announcer<PropertyChangeListener> announcer = Announcer.to(PropertyChangeListener.class);
    private final PropertyDiffTool diffTool = new PropertyDiffTool();

    public PropertyChangeEvent announceChanges(Properties previous, final Properties updated) {
            Iterable<PropertyComparison> comparison = diffTool.diffs(previous, updated);
        Sequence<PropertyComparison> modifiedProperties = sequence(comparison).filter(propertyValueChanged());

        PropertyChangeEvent event = PropertyChangeEvent.propertyChangeEvent(updated, modifiedProperties);
        if (event.containsChanges()) {
            announcer.announce().propertiesHaveChanged(event);
        }
        return event;
    }

    public void listen(PropertyChangeListener listener) {
        announcer.addListener(listener);
    }

    public void stopListening(PropertyChangeListener listener) {
        announcer.removeListener(listener);
    }
}
