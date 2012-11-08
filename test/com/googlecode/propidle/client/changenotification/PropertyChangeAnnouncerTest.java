package com.googlecode.propidle.client.changenotification;

import com.googlecode.propidle.properties.PropertyComparison;
import org.junit.Test;

import java.util.Properties;

import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertyComparison.changedProperty;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static com.googlecode.propidle.properties.PropertyValue.propertyValue;
import static com.googlecode.totallylazy.Pair.pair;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PropertyChangeAnnouncerTest {
    private final PropertyChangeAnnouncer announcer = new PropertyChangeAnnouncer();
    private final RecordingPropertyChangeListener recordingListener = new RecordingPropertyChangeListener();

    @Test
    public void shouldOnlyFireOffANotificationWhenAPropertyChanges() {
        Properties original = properties(
                pair("a", "1"),
                pair("b", "2")
        );
        Properties updated = properties(
                pair("a", "1"),
                pair("b", "9999999999")
        );

        announcer.listen(recordingListener);
        announcer.announceChanges(original, updated);

        assertThat(recordingListener.lastEvent().changes(), not(hasItem(propertyChanged("a", "1", "1"))));
        assertThat(recordingListener.lastEvent().changes(), hasItem(propertyChanged("b", "2", "9999999999")));
    }

    @Test
    public void shouldFireOffANotificationWhenAPropertyIsRemoved() {
        Properties original = properties(pair("a", "1"), pair("b", "2"));
        Properties updated = properties();

        announcer.listen(recordingListener);
        announcer.announceChanges(original, updated);

        assertThat(recordingListener.lastEvent().changes(), hasItem(propertyChanged("a", "1", null)));
        assertThat(recordingListener.lastEvent().changes(), hasItem(propertyChanged("b", "2", null)));
    }

    @Test
    public void shouldFireOffANotificationWhenAPropertyIsAdded() {
        Properties original = properties();
        Properties updated = properties(pair("a", "1"), pair("b", "2"));

        announcer.listen(recordingListener);
        announcer.announceChanges(original, updated);

        assertThat(recordingListener.lastEvent().changes(), hasItem(propertyChanged("a", null, "1")));
        assertThat(recordingListener.lastEvent().changes(), hasItem(propertyChanged("b", null, "2")));
    }

    @Test
    public void shouldNotSendEventsIfNoPropertiesHaveChanged() {
        Properties original = properties(pair("a", "1"), pair("b", "2"));

        announcer.listen(recordingListener);
        announcer.announceChanges(original, original);

        assertThat(recordingListener.lastEvent(), is(nullValue()));
    }

    private PropertyComparison propertyChanged(String name, String previous, String updated) {
        return changedProperty(propertyName(name), propertyValue(previous), propertyValue(updated));
    }
}
