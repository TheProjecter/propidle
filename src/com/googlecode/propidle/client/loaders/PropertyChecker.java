package com.googlecode.propidle.client.loaders;

import java.util.Properties;

public interface PropertyChecker {
    boolean exists(Properties properties, String property);

    class constructors {
        public static PropertyChecker manditory() {
            return new PropertyChecker() {
                @Override
                public boolean exists(Properties properties, String property) {
                    if(properties.getProperty(property) == null) throw new IllegalArgumentException(String.format("Property %s not found in %s", property, properties));
                    return true;
                }
            };
        }

        public static PropertyChecker optional() {
            return new PropertyChecker() {
                @Override
                public boolean exists(Properties properties, String property) {
                    return properties.getProperty(property) != null;
                }
            };
        }
    }
}
