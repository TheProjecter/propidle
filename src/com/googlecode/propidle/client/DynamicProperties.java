package com.googlecode.propidle.client;

import com.googlecode.propidle.client.changenotification.PropertyChangeAnnouncer;
import com.googlecode.propidle.client.changenotification.PropertyChangeEvent;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.propidle.client.loaders.PropertiesAtUrl.propertiesAtUrl;
import static com.googlecode.totallylazy.Sequences.sequence;

public class DynamicProperties implements PropertyChangeNotifier {
    private final PropertyChangeAnnouncer announcer;
    private final Sequence<Callable<Properties>> propertyLoader;

    private AtomicReference<Properties> lastSnapshot = new AtomicReference<Properties>(new Properties());

    public static DynamicProperties load(URL url) throws Exception {
        return load(propertiesAtUrl(url));
    }

    public static DynamicProperties load(Callable<Properties>... propertyLoader) throws Exception {
        DynamicProperties properties = new DynamicProperties(propertyLoader);
        properties.reload();
        return properties;
    }

    public DynamicProperties(Callable<Properties>... propertyLoader) {
        this.propertyLoader = sequence(propertyLoader);
        this.announcer = new PropertyChangeAnnouncer();
    }

    public synchronized PropertyChangeEvent reload() throws Exception {
        Properties previous = lastSnapshot.get();
        Properties updated = getFirstAvailableProperties();

        PropertyChangeEvent event = announcer.announceChanges(previous, updated);
        lastSnapshot.set(updated);
        return event;
    }

    private Properties getFirstAvailableProperties() {
        for (Callable<Properties> propertiesCallable : propertyLoader) {
            try{
                return propertiesCallable.call();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        throw new RuntimeException("Could not load properties");
    }

    public Properties snapshot() {
        if(lastSnapshot==null){
            throw new IllegalStateException("Properties have not been loaded. Call reload() first.");
        }
        return lastSnapshot.get();
    }

    public void listen(PropertyChangeListener listener) {
        announcer.listen(listener);
    }

    public void stopListening(PropertyChangeListener listener) {
        announcer.stopListening(listener);
    }
}
