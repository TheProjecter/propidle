package com.googlecode.propidle.client;

import com.googlecode.propidle.urls.SimpleUriGetter;
import com.googlecode.propidle.client.loaders.PropertiesAtUrl;
import com.googlecode.propidle.client.changenotification.PropertyChangeAnnouncer;
import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Callable;

public class DynamicProperties implements PropertyChangeNotifier {
    private final PropertyChangeAnnouncer announcer;
    private final Callable<Properties> propertyLoader;

    private Properties lastSnapshot = new Properties();

    public static DynamicProperties load(URL url) throws Exception {
        return load(PropertiesAtUrl.propertiesAtUrl(url, new SimpleUriGetter()));
    }

    public static DynamicProperties load(Callable<Properties> propertyLoader) throws Exception {
        DynamicProperties properties = new DynamicProperties(propertyLoader);
        properties.reload();
        return properties;
    }

    public DynamicProperties(Callable<Properties> propertyLoader) {
        this.propertyLoader = propertyLoader;
        this.announcer = new PropertyChangeAnnouncer();
    }

    public synchronized PropertyChangeEvent reload() throws Exception {
        Properties previous = lastSnapshot;
        Properties updated = propertyLoader.call();

        PropertyChangeEvent event = announcer.announceChanges(previous, updated);
        lastSnapshot = updated;
        return event;
    }

    public Properties snapshot() {
        if(lastSnapshot==null){
            throw new IllegalStateException("Properties have not been loaded. Call reload() first.");
        }
        return lastSnapshot;
    }

    public void listen(PropertyChangeListener listener) {
        announcer.listen(listener);
    }

    public void stopListening(PropertyChangeListener listener) {
        announcer.listen(listener);
    }
}
