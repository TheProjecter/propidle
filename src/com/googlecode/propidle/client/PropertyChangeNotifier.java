package com.googlecode.propidle.client;

public interface PropertyChangeNotifier {
    void listen(PropertyChangeListener listener);
    void stopListening(PropertyChangeListener listener);
}
